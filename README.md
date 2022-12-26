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

| Parameter           | Beschreibung                                     |
|---------------------|--------------------------------------------------|
| `<filename>`        | Der Name der einzulesenden Datei                 |
