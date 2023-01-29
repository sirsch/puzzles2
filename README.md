# SA4E, Winter 2022

Abgabe zu Übungsblatt 2, Sebastian Irsch, 1337932

Diese Abgabe basiert auf der Abgabe zu Übungsblatt 1 https://github.com/sirsch/puzzles

## zu Aufgabe 1

Die Erweiterung für das aktuelle Übungsblatt besteht aus den Implementierungen der Ausgabeverfahren,
der Integration in den Lösealgorithmus, eine Steuerungskomponente zum Wechsel der Ausgabeverfahren
und der Implementierung der Dependency-Injection.

### Implementierungsbeschreibung

Weil die verschiedenen Ausgabeverfahren an derselben Stelle in den `PuzzleSolver` injiziert werden
sollen, wurde für diese Ausgabeverfahren die gemeinsame Schnittstelle `SolverProgressLogger`
definiert. Für die Ausgabe auf dem Bildschirm wurde ein Logging auf die Standardausgabe vorgesehen.
Die Konsolenausgabe und die Dateiausgabe werden beide über den `PrintStreamSolverProgressLogger`
realisiert. Die Fabrik `SolverProgressLoggerFactory` setzt Wahlweise `System.out` oder ein
Datenstrom zur Ausgabe in die Datei ein. Für die Ausgabe an einen MQTT-Server wird die Klasse
`MqttSolverProgressLogger` verwendet.

Um die Magie der reflektiven Dependency-Injection zu demonstrieren werden Referenzen in Felder
injiziert. Die Klasse `PuzzleSolver` enthält dennoch eine Setter-Methode für `SolverProgressLogger`,
damit die Funktionalität in Unit-Tests geprüft werden kann. Allgemein ist es empfehlenswert
Constructor-Injection oder Method-Injection zu verwenden, damit der resultierende Code einfach in
Unit-Tests geprüft werden kann.

Zum Umschalten der Status-Ausgabe werden Befehle von der Standardeingabe gelesen. Weil der
Main-Thread mit dem Lösen des Rätsels beschäftigt ist, muss für das Lesen der Eingabe ein weiterer
Thread gestartet werden. Der `LogOutputManager` startet dazu einen Thread im Daemon-Modus, der beim
Beenden von Main automatisch gestoppt wird. Die Befehle zum Wechsel der Ausgabe werden von
`System.in` gelesen. Zur Übertragung des Ausgabeverfahrens an die aktive Instanz von `PuzzleSolver`
wird der `Injector` verwendet. Weil die Lösung des Rätsels typischerweise in wenigen 100
Millisekunden ermittelt wird, bietet die Methode `LogOutputManager#awaitFirstSelection` die
Möglichkeit auf die erste Auswahl eines Ausgabeverfahrens zu warten.

Die Verwendung eines zweiten Threads macht es Erforderlich, dass die Übergabe des Ausgabemechanismus
an den Main-Thread nebenläufigkeitssicher durchgeführt wird. Das Java Memory Model, kann im Rahmen
dieser Implementierungsbeschreibung nicht in seiner ganzen Tiefe behandelt werden. Als Merksatz
gilt: Ein `volatile`-Zugriff ist ein halbes `synchronized` bezüglich der Sichtbarkeit von Änderungen
am Heap. Ein volatile Read hat dieselben Auswirkungen wie das Eintreten in einen synchronized-Block
und ein volatile Write hat dieselben Auswirkungen wie das Verlassen eines synchronized-Blocks. Das
heißt für Java-Versionen >= 5, dass der das Feld lesende Thread alle Änderungen sieht, die der
schreibende Thread vor dem volatile Write auf das Feld getätigt hatte.

Die Injection soll mittels Java Reflection API umgesetzt werden. Damit kann man auf die Struktur
von Klassen, wie beispielsweise Felder, Methoden und Konstruktoren zugreifen. Was in Java über die
Reflection API nicht möglich ist, ist Objektinstanzen von Klassen abzufragen. Daher müssen die
Instanzen von `PuzzleSolver`. Das Spring-Framework verwendet den Application-Context als Container
für die erzeugten Instanzen, hier (Spring-)Beans genannt. Für dieses Übungsblatt wurde eine
Factory für die Solver-Instanzen bereitgestellt, die die Referenzen der erzeugten Instanzen
aufzeichnet, damit diese später zugreifbar sind. Ferner kann man der `PuzzleSolverFactory` vorab
die Injector-Aktion übergeben, damit diese auf neue Instanzen angewendet werden kann.

Der eigentliche Reflection-Mechanismus ist in der Klasse `Injector` implementiert. Zur Markierung
der für Injection infrage kommenden Felder wurde die Annotation `@Injectable` definiert. Der
Injector durchsucht die Klasse des Zielobjekts nach Feldern, die als Injectable markiert sind und
einen passenden Typ aufweisen. Falls das Feld aufgrund der Sichtbarkeit nicht zugänglich ist, wird
es per `#setAccessible` zugänglich gemacht. Im Anschluss daran wird der Wert gesetzt.

### Verwendung

Um das Umschalten während des Lösens zu ermöglichen, kann das Kommando mit einem Delay-Parameter
aufgerufen werden, um die Ausführung zu Verzögern. Für jede Permutation wird zusätzlich delay
Millisekunden verzögert.

#### Lösen von Rätseln mit Verzögerung
Mit dem Kommando `solve-puzzle <filename> <?delay>` kann ein Rätsel aus einer Datei gelesen und
gelöst werden. Dabei wird für jede Permutation eine Verzögerung von delay Millisekunden eingefügt.

| Parameter    | Beschreibung                                            |
|--------------|---------------------------------------------------------|
| `<filename>` | Der Name der einzulesenden Datei                        |
| `<?delay>`   | (optional) Verzögerung pro Permutation in Millisekunden |

#### Auswahl der Ausgabe während der Programmausführung

Während der Ausführung des Solve-Puzzle-Commands fragt die Anwendung die zu verwendende Ausgabe ab.
Per Texteingabe und Bestätigung mit Enter kann ein neuer Ausgabemechanismus gewählt werden.

| Befehl             | Beschreibung                                                        |
|--------------------|---------------------------------------------------------------------|
| `none`             | Ausgabe deaktivieren                                                |
| `stdout`           | Ausgabe nach stdout (Standardausgabe)                               |
| `file=<filename>`  | Ausgabe in die Datei die mittels 'filename' angegeben wurde         |
| `mqtt=<serverURI>` | Ausgabe an einen MQTT-Server der per 'serverURI' spezifiziert wurde |

Ein Beispiel für eine MQTT-Server-URI ist `tcp://localhost:1883`.

## zu Aufgabe 2

Bei Aufgabe 2 der zweiten Übung geht es darum, die individuellen Lösungen aus Übung 1 zu Verbinden.
Dazu wurde eine nachrichtenbasierte Kommunikation auf Basis eines MQTT-Servers vorgesehen. Das
JSON-Format der Nachrichten wurde vorgegeben. Als Grundlage für die Umsetzung der Prozesse soll
Apache Camel verwendet werden.

Jeder Teilnehmer soll das MQTT-Topic 'Zahlenraetsel' abonnieren, um von dort Aufgaben im gemeinsamen
Austauschdatenformat zu beziehen. Die Rätsel sollen dann in das anwendungsspezifische
gRPC-/Protobuf-Format konvertiert werden, und an den entsprechenden gRPC-Dienst zur Lösung des
Rätsels übergeben werden. Zum Schluss muss dann die Lösung, die als gRPC-Antwort zurückgesandt wurde
wieder in das Austauschdatenformat konvertiert werden und an das MQTT-Topic 'Loesung' gesendet
werden. Zusätzlich soll es möglich sein, dass ausgewählte Teilnehmer regelmäßig Rätsel erzeugen und
im Austauschdatenformat an das Topic 'Zahlenraetsel' senden.

### Implementierungsbeschreibung

Die Lösung zu Aufgabe 2 wurde im selben Repositoriy realisiert, das auch für Aufgabe 1 genutzt wird.
Zusätzlich wurde das Kommando `RunCamelCommand` hinzugefügt, um den Camel-Server zu starten. Zum
Betrieb ist es folglich erforderlich, zwei Instanzen der Anwendung zu betreiben, einmal den
Camel-Server und einmal den gRPC-Service aus Übung 1.

Die Camel-Anwendung besteht hauptsächlich aus der Routen-Konfiguration und der Verwaltung des
Lifecycle des Camel-Kontextes. Spezielle Fehlerbehandlungsmechanismen wurden für die Übungsaufgabe
nicht etabliert.

Für die Lösung der Aufgabe 2 wurden zwei Camel-Routen erstellt.

#### Route zum Lösen von Rätseln

Die erste hier beschriebene Route ist die zum Lösen von Rätseln. Sie nimmt Nachrichten von MQTT
entgegen, formatiert diese um und sendet diese an den gRPC-Dienst zur Lösungsberechnung. Das
Ergebnis vom Dienst wird dann nochmal umformatiert und als Lösung an MQTT übertragen. Das folgende
Code-Beispiel zeigt die Routenkonfiguration.

```java
from(createMqttUri("Zahlenraetsel"))
        .unmarshal().json(JsonLibrary.Jackson, CommonSolvePuzzleRequest.class)
        .process(RunCamelCommand.this::convertCommonFormat2Protobuf)
        .to(createGrpcSolvePuzzleUri())
        .filter(RunCamelCommand.this::isSolutionFound)
        .process(RunCamelCommand.this::mergeResult)
        .marshal().json(JsonLibrary.Jackson, true)
        .to(createMqttUri("Loesung"));
```

* Die Quelle ist hier die Camel-MQTT-URI. Die URI
  `"paho-mqtt5:" + topic + "?brokerUrl=" + this.mqttBrokerUrl` gibt an, dass die Camel-Component
  paho-mqtt5 verwendet werden soll. Ferner gibt sie das Topic als Pfad und die URL des MQTT-Servers
  als Parameter an.
* Der erste Prozessschritt mappt eine Nachricht auf die Klasse CommonSolvePuzzleRequest unter
  Verwendung der Json-Bibliothek Jackson. Dieser Schritt wird allgemein Unmarshalling genannt und
  besitzt eine eigene Methode in der RouteBuilder-API von Camel.
* Die Konvertierung vom Austauschdatenformat (CommonSolvePuzzleRequest) in das
  implementierungsspezifische Protocol-Buffer-Objekt ist die erste anwendungsspezifische
  Prozessstufe. Einfache Prozessoren können per Lambda-Ausdruck oder Method-Reference in die Route
  integriert werden. Erst ab einer bestimmten Komplexität lohnt sich die Umsetzung einer
  vollständigen Camel-Component.
* Das erste Ziel ist der gRPC Service, das über die URI `"grpc://" + this.grpcServer`
  `+ "/software.sirsch.sa4e.puzzles.protobuf.PuzzleSolver?method=solvePuzzle&synchronous=true"`
  in die Route integriert wird. Dies ist eine Anweisung an die gRPC-Component von Camel, einen RPC
  auszuführen. Die Typhierarchie von Camel offenbart, dass `Producer` immer auch `Prcessor`s sind.
  Daher ist es nicht verwunderlich, dass die to-Methode der RouteBuilder-API auch inmitten einer
  Route vorkommen kann und nicht, wie man naiverweise annehmen könnte nur als Ende. Im Falle der
  gRPC-Component wird der Rückgabewert des RPC als Nachrichteninhalt weitergereicht.
* Da der Fall eines unlösbaren Rätsels in der Aufgabenstellung und im Datenformat nicht spezifiziert
  sind, werden unlösbare Rätsel in nächsten Schritt mit einem Filter herausgefiltert.
* Der nächste Prozessschritt führt die Lösung mit dem ursprünglichen Rätsel zusammen. Der hier
  verwendete gRPC-Dienst gibt nicht das komplette gelöste Rätsel zurück, sondern nur die Zuordnung
  von Symbol auf ermittelt Ziffer. Das Datenaustauschformat für Lösungen sieht jedoch die Rückgabe
  des gelösten Rätsels vor. Daher wird an dieser Stelle das ursprüngliche Rätsel genötigt. Seit dem
  gRPC-Call wird das Rätsel jedoch nicht mehr im Nachrichteninhalt weitergegeben. Die Prozessstufen
  sollen des Weiteren unabhängig voneinander und zustandslos arbeiten, sodass das Rätsel nicht über
  eine gemeinsame / geteilte Variable übergeben werden darf. Hier bietet sich die Verwendung von
  Nachrichten-Headern an. Header sind Schlüssel-Wert-Paare die Metadaten von Nachrichten
  transportieren können. Daher wurde die erste Prozessstufe `convertCommonFormat2Protobuf` so
  programmiert, dass sie jeweils das ursprüngliche Rätsel im Datenaustauschformat und den aktuellen
  Zeitstempel als Header der Nachricht anfügt. Die Prozessstufe `mergeResult` kann somit das
  Ergebnis des Rätsels herstellen und die Dauer berechnen.
* Die letzte Konvertierung ist ein Marshalling nach Json in das Datenaustauschformat für Lösungen.
  Hier wird die Pretty-Print-Option genutzt, damit die Kommunikation einfacher mit einem MQTT-Client
  mitgelesen werden kann.
* Als Abschluss der Route wird die Lösung an das MQTT-Topic 'Loesung' gesendet.

#### Route zur Generierung von Rätseln

Eine weitere Route, die optional eingebunden werden kann, erzeugt regelmäßig neue Rätsel und sendet
diese an den MQTT-Server.

```java
from("timer:puzzleGenerator?period=60000")
		.process(RunCamelCommand.this::generatePuzzle)
		.marshal().json(JsonLibrary.Jackson, true)
		.to(createMqttUri("Zahlenraetsel"));
```

* Quelle für diese Route ist ein Timer aus der gleichnamigen Camel-Component. In diesem Fall wird
  alle 60 Sekunden eine Nachricht ausgelöst.
* Die erste Prozessstufe erzeugt ein Rätsel, das bereits in das Austauschdatenformat gebracht wurde.
* Per Marshalling wird die Nachricht JSON-kodiert. Auch hier wieder mit Pretty-Print zur besseren
  Lesbarkeit.
* Abschließend wird die Nachricht an das MQTT-Topic 'Zahlenraetsel' gesendet.

### Verwendung

Zusätzlich zu den Kommandos aus Übungsblatt 1 https://github.com/sirsch/puzzles, wird das Kommando
`run-camel` bereitgestellt.

#### Camel-Server laufen lassen

Mit dem Kommando `run-camel <mqttBrokerUrl> <grpcServer> <?generatePuzzleNumberOfDigits>` wird der
Camel-Server gestartet. Die Anwendung wartet dabei so lange, bis per Enter-Taste das Herunterfahren
des Camel-Kontextes eingeleitet wird.

| Parameter                         | Beschreibung                                               |
|-----------------------------------|------------------------------------------------------------|
| `<mqttBrokerUrl>`                 | Die URL des MQTT-Servers                                   |
| `<grpcServer>`                    | Host und Port des gRPC-Service (z. B. localhost:12345)     |
| `<?generatePuzzleNumberOfDigits>` | (optional) Anzahl der Stellen von zu generierenden Rätseln |

Wenn der Parameter `<?generatePuzzleNumberOfDigits>` angegeben wurde, wird die Route zur
zeitgesteuerten Erzeugung von Rätseln registriert und damit Camel zur Ausführung übergeben.

### Fazit

Die Einarbeitung in Apache Camel ist etwas holprig, aber es lohnt sich. Die Arbeit das Kamel zu
zähmen rentiert sich, weil es dann als Nutztier genügsam und fleißig seinen Dienst tut.
