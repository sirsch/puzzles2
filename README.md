# SA4E, Winter 2022

Abgabe zu Übungsblatt 1, Sebastian Irsch, 1337932

## zu Aufgabe 1

Wie im Übungsblatt erlaubt, wurden die Aufgaben 1 und 2 zusammen gelöst und das Ergebnis als ein
github-Repository abgegeben.

### Implementierungsbeschreibung

Zunächst zur Beschreibung des für Aufgabe 1 relevanten Teils. Vorausschauend auf Aufgabe 2 kann
festgestellt werden, dass eine Kodierung der Daten mittels Protocol Buffers bereitgestellt werden
soll, weil das die übliche Kodierung für gRPC ist. Daher bietet es sich an, dieses Format bereits
im ersten Aufgabenteil zu verwenden.

Die Anwendung, die als Lösung zu Aufgabe 1 programmiert wurde, unterscheidet zwischen dem
internen Datenmodell, bei dem den Symbolen Werte zugewiesen und Berechnungen durchgeführt werden
können, und dem Wire-Format, das lediglich der Speicherung und Übertragung von Rätseln dient. Das
interne Modell ist dabei für eine effiziente Lösung von Rätseln optimiert. Da dieses Modell jedoch
auf Objektreferenzen basiert, jedes Vorkommen eines Symbols verweist auf dieselbe Instanz, lässt es
sich nicht mit den gängigen Kodierungsmechanismen serialisieren. XML kennt noch den Begriff der
Identität für Objekte. Dazu verwendet es IDs im Dokument. Das ist zwar mächtig, aber auch unhandlich
und die meisten moderneren Formate wie JSON, Yaml oder Protobuf konzentrieren sich auf die Abbildung
von baumartigen Objektstrukturen. Daher wurde im Entwicklungsprozess die Design-Entscheidung
getroffen, das Dateispeicher- und Übertragungsformat von der internen Repräsentation zu trennen.
Damit sind die Aufgaben und die Designvorgaben klar getrennt. Während das interne Datenmodell
bestehend aus den Klassen `Puzzle`, `Addition`, `Cell` und `Symbol` eine objektorientierte Abbildung
der Problemstellung ist, dient das Protocol-Buffers-basierte Wire-Format einer möglichst einfachen
Repräsentation der zu übertragenden Daten. Dieses spezielle Übertragungsformat ist nicht besonders
restriktiv umgesetzt. Es ist nicht Sinn und Zweck, alle Regeln eines Zahlenrätsels in das
Wire-Format zu kodieren. Mit dem Wire-Format könnten beispielsweise ungültige Rätsel übertragen
werden. Die Details werden erst bei der Umwandlung durch `Protobuf2PuzzleConverter` in das interne
Format geprüft. Weil die interne Datenstruktur des Rätsels, die aus mehreren Klassen besteht,
aufwändig zu erzeugen ist, wird hier das Builder-Muster verwendet. Der `PuzzleBuilder` ermöglicht 
es, ein `Puzzle` zu erzeugen. In der build-Methode werden die übergebenen Symbole und Zellen auf
Konsistenz geprüft. Die Klasse `PuzzleSolver` dient der Ermittlung von Lösungen zu den Rätseln
mittels Brute-Force-Ansatz. Zum Erzeugen von Rätseln gibt es die Klasse `PuzzleGenerator`.

### Verwendung

Das Programm ist mit einer einzigen Main-Methode ausgestattet. Die gewünschte Funktionalität wird
durch den Kommandonamen, der als erster Parameter zu übergeben ist, festgelegt. Nach dem Namen des
Kommandos können weitere Parameter folgen. Für Aufgabe 1 sind die beiden Kommandos `generate-puzzle`
und `solve-puzzle` relevant.

#### Erzeugung von Rätseln
Mit dem Kommando `generate-puzzle <filename> <?numberOfDigits>` kann ein neues Rätsel erzeugt und
als Datei abgespeichert werden.

| Parameter           | Beschreibung                                     |
|---------------------|--------------------------------------------------|
| `<filename>`        | Der Name der zu erzeugenden Datei                |
| `<?numberOfDigits>` | (optional) Die Anzahl der zu erzeugenden Stellen |

#### Lösen von Rätseln
Mit dem Kommando `solve-puzzle <filename>` kann ein Rätsel aus einer Datei gelesen und gelöst
werden.

| Parameter    | Beschreibung                     |
|--------------|----------------------------------|
| `<filename>` | Der Name der einzulesenden Datei |

## zu Aufgabe 2

Weil gRPC das Thema von Aufgabe 2 ist, wird hier zunächst näher auf die Code-Generierung mit dem
Protobuf-Compiler eingegangen. Dann wird die allgemeine Architektur beschrieben und schließlich
die Verwendung des Programms in Bezug auf die Client-Server-Kommunikation erläutert.

### Code-Generierung mit `protoc`

Auch der Protocol-Buffer-Code aus Aufgabe 1 wurde bereits mit dem Protobuf-Compiler `protoc`
erzeugt. Mit dem gRPC-Plugin für protoc können die Stubs und die Basisklasse für die
Server-Implementierung erzeugt werden. Der Code für die Protocol-Buffers-Nachrichten und gRPC kann
in derselben protobuf-Datei verfasst werden. Im Rahmen dieses Aufgabenblatts wurde der Code in die
Datei `Puzzles.proto` geschrieben. Für den generierten Code wurde ein separates Java-Paket
vorgesehen, um eine klare Trennung zwischen generiertem und selbstgeschriebenem Code zu
bewerkstelligen.

Mit dem Kommando `protoc --java_out=src/main/java
src/main/resources/software/sirsch/sa4e/puzzles/protobuf/Puzzles.proto` wurde die Code-Generierung
für die Nachrichten ausgelöst. Dabei wurde die Klasse
`software.sirsch.sa4e.puzzles.protobuf.Puzzles` und die Nachrichten als nested Classes erzeugt. Für
die Erzeugung der gRPC-Stubs und der Basisklasse für den Dienst wurde `protoc
--plugin=protoc-gen-grpc-java=protoc-gen-grpc-java-1.51.1-linux-x86_64.exe
--grpc-java_out=src/main/java
src/main/resources/software/sirsch/sa4e/puzzles/protobuf/Puzzles.proto` verwendet.

### Implementierungsbeschreibung

Die Klasse `PuzzleSolverService` ist von der generierten, abstrakten Klasse
`PuzzleSolverGrpc.PuzzleSolverImplBase` abgeleitet und fügt die Business-Logik in das generierte
Gerüst ein. Für das Lifecycle-Management des Servers ist die Klasse `PuzzleSolverServer` zuständig. 
Weil es sich bei der Abgabe um eine Konsolenanwendung handelt, wurde, wie im Tutorial
https://grpc.io/docs/languages/java/basics/, ein Shutdown-Hook zum Reagieren auf ein Signal
verwendet. Der Main-Thread wartet derweil auf das Herunterfahren des gRPC-Servers. Man könnte den
automatisch generierten Stub `PuzzleSolverGrpc.PuzzleSolverBlockingStub` als Client
verwenden. Dann müsste sich der Aufrufer um die Behandlung des gRPC-Channels kümmern. Insbesondere
muss der `Channel` heruntergefahren werden, um Ressourcen freizugeben. Um die Ressourcen-Behandlung
auf Client-Seite zu vereinfachen, wurden Channel und Blocking-Stub hinter der Schnittstelle der
Klasse `PuzzleSolverClient` verborgen. Diese Klasse implementiert die Schnittstelle `Closeable`,
sodass sie in einem Try-With-Resources verwendet werden kann.

### Verwendung

Der Anwendung aus Aufgabe 1 wurden zwei weitere Kommandos hinzugefügt. Mit `run-server` kann ein
Server zum Lösen von Rätseln gestartet und betrieben werden. Das Kommando `request-solve-puzzle`
erzeugt ein neues Rätsel und sendet es zur Lösung an einen Server. Die Antwort wird nach Empfang auf
dem Client ausgegeben.

#### Starten des Servers
Mit dem Kommando `run-server <port>` wird ein Server gestartet und betrieben. Das Programm läuft
dabei so lange, bis es von außen zum Beispiel mit `STRG` + `C` beendet wird. Beim Aufruf aus einer
IDE wie Intellij wird das Programm mit der Stop-Schaltfläche beendet.

| Parameter | Beschreibung                                                  |
|-----------|---------------------------------------------------------------|
| `<port>`  | Der Port, auf dem der Server Verbindungen entgegennehmen soll |

#### Rätsel erzeugen und von einem Server lösen lassen
Mit dem Kommando `request-solve-puzzle <serverHost> <serverPort> <?numberOfDigits>` wird ein neues
Rätsel erzeugt und zur Lösung an den Server übertragen. Die Antwort des Servers wird im Anschluss
daran auf dem Client ausgegeben.

| Parameter           | Beschreibung                                            |
|---------------------|---------------------------------------------------------|
| `<serverHost>`      | Der Hostname des Servers                                |
| `<serverPort>`      | Der Port, auf dem der Server Verbindungen entgegennimmt |
| `<?numberOfDigits>` | (optional) Die Anzahl der zu erzeugenden Stellen        |

## Spring Boot

Das Maven-Projekt dieser Abgabe basiert auf der Parent-POM vom Spring Boot. Diese wird verwendet,
weil das Spring-Boot-Projekt eine nützliche und aufeinander abgestimmte Auswahl von gängigen
Java-Bibliotheken und dem Test-Framework Junit 5 als Abhängigkeiten liefert. Die Funktionalität von 
Spring Boot oder Spring Framework wird nicht verwendet.
