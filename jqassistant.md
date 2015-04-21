Die Klasse NotListedConverter implementiert PropertyConverter, ist 
aber nicht als ServiceProvider eingetragen.

Die Service Configuration org.apache.tamaya.spi.Bla gibt es nicht
als Service, sprich, es gibt keine Klasse org.apache.tamaya.spi.Bla.

Die Service Configuration org.apache.tamaya.spi.Bla führt
die Klasse org.apache.tamaya.spi.BlaImpl auf. Diese gibt es auch nicht.

Wie kann ich diese Fehler mittels jQAssistant und Cypher finden? 

Beispielsweise würde ich gerne fragen:

Welche Klassen implementieren einen Service, sind aber nicht 
in mindestens einer Service Configuration eingetragen?

Hier noch das Concept, welches ich anwende:

    <concept id="module:SPI">
        <description>Labels all .spi packages as "SPI".</description>
        <cypher><![CDATA[
			MATCH
				(package:Package)

			WHERE
				package.name = "spi"

			SET
			    package:SPI

			RETURN
				package
        ]]></cypher>
    </concept>

Eine weitere Frage wäre, ob es eine Service Configuration gibt,
zu der es keinen Service gibt?

Oder enthält eine Service Configuration einen Provider, den 
es nicht gibt?

