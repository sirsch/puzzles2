# SA4E, Winter 2022

Abgabe zu Übungsblatt 2, Sebastian Irsch, 1337932

Diese Abgabe basiert auf der Abgabe zu Übungsblatt 1 https://github.com/sirsch/puzzles

## zu Aufgabe 1

Die Erweiterung für das aktuelle Übungsblatt besteht aus den Implementierungen der Ausgabeverfahren,
der Integration in den Lösealgorithmus, eine Steuerungskomponente zum Wechsel der Ausgabeverfahren
und der Implementierung der Dependency-Injection.

### Implementierungsbeschreibung

Weil die verschiedenen Ausgabeverfahren an der selben Stelle in den `PuzzleSolver` injiziert werden
sollen, wurde für diese Ausgabeverfahren die gemeinsame Schnittstelle `SolverProgressLogger`
definiert. Für die Ausgabe auf dem Bildschirm wurde ein Logging auf die Standardausgabe vorgesehen.
Die Konsolenausgabe und die Dateiausgabe werden beide über den `PrintStreamSolverProgressLogger`
realisiert. Die Fabrik `SolverProgressLoggerFactory` setzt Wahlweise `System.out` oder ein
Datenstrom zur Ausgabe in die Datei ein. Für die Ausgabe an einen MQTT-Server wird die Klasse
`MqttSolverProgressLogger` verwendet.

Um die Magie der reflektiven Dependency-Injection zu demonstrieren werden Referenzen in Felder
injiziert. Um die Klasse `PuzzleSolver` enthält dennoch eine Setter-Methode für
`SolverProgressLogger`, damit die Funktionalität in Unit-Tests geprüft werden kann. Allgemein ist es
empfehlenswert Constructor-Injection oder Method-Injection zu verwenden, damit der resultierende
Code einfach in Unit-Tests geprüft werden kann.

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
einen Passenden Typ aufweisen. Falls das Feld aufgrund der Sichtbarkeit nicht zugänglich ist, wird
es per `#setAccessible` zugänglich gemacht. Im Anschluss daran, wird der Wert gesetzt.

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
