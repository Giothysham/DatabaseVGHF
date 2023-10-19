# DatabaseVGHF van Mauro Vranckx & Edmond Tsampanis
## Inleiding
Voor het vak Databases [DAB-4290] is er gevraagd om een database te maken voor de Video Game History Foundation (VGHF). Hierbij is er vooral gefocust op de verkoop van games en verzamelobjecten. Om dit beter te visualiseren is er ook een domein model schema gemaakt die terug valt te vinden op het einde van dit bestand. De database die hier voorgesteld wordt bevat zes entiteiten in totaal en zijn als volgt: 
-	VGHF (winkels)
-	games
-	extra
-	genre 
-	publisher
-	klant.

Deze worden afzonderlijk nog is besproken om te zien om het verband tussen de verschillende entiteiten uit te leggen en de eigenschappen per entiteit.

## VGHF winkels
De entiteit winkels bevatten de volgende eigenschappen: 
-	bezoekers 
-	geld
-	gID (game identificatie) ook een lijst van games? Zie lijst klanten 
-	wID (winkel identificatie) locatie bij games en extra veranderd naar wID
-	locatie.

De eerste twee eigenschappen van de VGHF-entiteit dienen ervoor om te zien als de winkel zelf wel effectief nut heeft. Hieruit kan beredeneerd worden als deze winkel genoeg geld opbrengt en bezoekers krijgt om zichzelf stabiel te houden zonder externe help. Verder zijn er de game en winkel identificaties. Over het algemeen worden identificaties gebruikt als een kortere referentie naar het effectieve object. In dit geval zou de wID gebruikt kunnen worden om de locatie te achterhalen zodat verschillende winkels niet met elkaar verward worden. Hetzelfde idee geld voor de gID. In plaats van de volledige naam van een game op te zoeken kan de gID opgezocht worden.

## Games
De entiteit games bevatten de volgende eigenschappen:
-	aantal in stock
-	aantal uitgeleend
-	console 
-	gID
-	wID
-	kostprijs
-	uID (klant identificatie)
-	gID2 (genre identificatie) 
-	pID (publisher identificatie).


## Extra
De entiteit extra bevatten de volgende eigenschappen:
-	aantal in stock
-	aantal verkocht
-	eID (extra identificatie)
-	wID
-	kostprijs
-	uID Waarom houden we bij aan wie we iets verkocht hebben?
-	type.

## Publisher
De entiteit publisher bevatten de volgende eigenschappen:
-	naam
-	beschrijving
-	pID.

## Genre
De entiteit genre bevatten de volgende eigenschappen:
-	naam
-	beschrijving
-	gID2.

## Klant
De entiteit klant bevatten de volgende eigenschappen:
-	achternaam
-	voornaam
-	telefoonnummer
-	uID
-	e-mail.

## Relaties 






{
•	Te doen
•	Opmerkingen
•	veranderingen
Verwijder dit
}

