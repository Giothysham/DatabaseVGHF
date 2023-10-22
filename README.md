# DatabaseVGHF van Mauro Vranckx & Edmond Tsampanis
## Inleiding
Voor het vak Databases [DAB-4290] is er gevraagd om een database te maken voor de Video Game History Foundation (VGHF). Hierbij is er vooral gefocust op de verkoop en verhuur van games en verzamelobjecten. Om dit beter te visualiseren is er ook een domein model schema gemaakt die terug kan gevonden worden het einde van dit documentatie. De database die hier voorgesteld wordt bevat zes entiteiten en zijn als volgt: 
-	VGHF (winkels)
-	games
-	extra
-	genre 
-	uitgever 
-	klant.

Deze worden afzonderlijk nog is besproken om te zien wat het verband is tussen de verschillende entiteiten en de eigenschappen die elke entiteit bevat. 

## Entiteiten
### VGHF winkels
De entiteit winkels bevatten de volgende eigenschappen: 
-	bezoekers 
-	omzet
-	wID (winkel identificatie) 
-	locatie.

De eerste twee eigenschappen van de VGHF-entiteit dienen ervoor om te zien als de winkel zelf wel effectief nut heeft. Hieruit kan een omzet berekend worden die verteld of deze winkel genoeg geld opbrengt en bezoekers krijgt om zichzelf stabiel te houden zonder externe help. Verder is er ook nog een wID of een winkel ID, deze geeft de mogelijkheid om een game of verzamelobjecten te kopellen aan een winkel. Hiermee kan er een inventaris van de winkel bijgehouden worden.

### Games
De entiteit games bevatten de volgende eigenschappen:
-	aantal in stock
-	aantal uitgeleend
-	console type 
-	gaID (game identificatie)
-	wID
-	kostprijs
-	kID (klant identificatie)
-	geID (genre identificatie)
-	uID (uitgever identificatie).	

Voor games is er algemene informatie ter beschikking maar ook specifiekere informatie. Een voorbeeld hiervan is de wID die verteld waar deze game ter beschikking is en het aantal in stok die verteld hoeveel. Ook is hier een kID aan gekoppeld om te weten aan wie de game is uitgeleend. Zodat wanneer de game niet is teruggebracht, de verhuurder weet welke klant gecontacteerd moet worden. Het genre ID geeft de klant de mogelijkheid om op een website of app te filteren tussen verschillende type van spellen. Hetzelfde geld voor de uitgever ID.

### Extra
De entiteit extra bevatten de volgende eigenschappen:
-	aantal in stock
-	aantal verkocht
-	gaID 	
-	eID 
-	wID 
-	uID	
-	kostprijs
-	type.

Een groot deel van de eigenschappen die vermeld werden bij games komen hier bij extra’s ook aan bod. Eén groot verschil is dat hier een type aan is gebonden. Dit typen laat zien wat voor product verkocht is, voorbeelden hiervan zijn magazines of actiefiguren. Ook hier kan opgemerkt worden dat het aantal verkochte producten bijgehouden wordt. Dit kan een VGHF helpen te beslissen of ze een nieuwe bestelling plaatsen van een bepaalde extra. De gaID bij de extra mag NULL zijn, deze wordt hier meegegeven omdat soms een extra kan verbonden zijn aan een specifieke game.

### Uitgever
De entiteit Uitgever bevatten de volgende eigenschappen:
-	naam
-	beschrijving
-	uID.

Deze wordt bijgehouden zodat da klant iets meer info krijgt over een game studio die hij of zij niet kent, maar kan ook gebruikt worden om te bekijken welke game studio het meeste verhuurd. 

### Genre
De entiteit genre bevatten de volgende eigenschappen:
-	naam
-	beschrijving
-	geID.

Het genre laat de klant kiezen wat voor game hij of zij zoekt aan de hand van het toedienen van filters op een webpagina of app. Deze wordt per game bijgehouden. 

### Klant
De entiteit klant bevatten de volgende eigenschappen:
-	achternaam
-	voornaam
-	telefoonnummer
-	kID
-	e-mail
-	adres
-	stad
-	provincie
-	land
-	post code.

Van de klant wordt er verwacht dat hij of zij de benodigde gegevens doorgeeft zodat wanneer er een spel niet wordt terug gebracht die persoon bereikt kan worden. Ook kan de e-mail gebruikt worden voor het versturen van nieuws letters wanneer de klant hiermee instemt. Het adres wordt gevraagd zodat er een factuur naar de klant verstuurd kan worden.  

### Factuur 
De entiteit factuur bevat de volgende eigenschappen:
-	fID (factuur identificatie)
-	prijs
-	kID
-	wID
-	gID
-	eID.

Een factuur bevat waar de klant voor heeft betaald, vanwaar de klant iets koopt en de prijs waar voor hij of zij wordt gefactureerd. Het facturering adres kan opgevraagd worden via de kID.  

## Relaties 
In het totaal zijn er 6 entiteiten waarvan de connecties tussen de voorgemelde hier besproken wordt. De eigenschappen van elke entiteit kan in het vorige hoofdstuk of in het schema op het einde teruggevonden worden. Alles begint met een lijst van alle VGHF-winkels. Deze VGHF-winkels zijn verbonden met games en extra’s. Wat waargenomen kan worden is dat al deze entiteiten de eigenschap van een wID bevatten. Deze wID zorgt ook voor de connectie tussen deze entiteiten en toont aan waar de game of extra kan gevonden worden. Zo wordt er een veel-naar-veel relatie bekomen omdat meerdere winkels meerdere games kunnen hebben, maar ook meerdere games op meerdere locaties verkregen kunnen worden. Door de locatie en het aantal per game en extra te weten, kan deze informatie ook gebruikt worden voor een herverdeling van producten tussen winkels.

Een game bevat drie eigenschappen die een verbinding tussen drie andere entiteiten kan vormen. Deze zijn als volgt:
-	geID
-	kID 
-	uID. 

De geID of genre ID is een nul-naar-veel relatie. Dit komt omdat een game niet altijd een genre heeft. Het idee hierrond is zo veel mogelijk informatie ter beschikking te stellen, maar tegelijkertijd ook niet ervoor te zorgen dat producten niet verhandeld kunnen worden door te kort aan overbodige informatie. Ook is een genre altijd met een game verbonden omdat deze anders overbodig is. De relatie tussen de uitgever en de game is een één-naar-veel relatie, dit komt omdat een game niet kan bestaan zonder een uitgever. Maar een uitgever kan wel meerdere games maken. 
Hetzelfde idee geld voor de extra’s. Hier zijn twee eigenschappen die twee verschillende entiteiten verbinden met extra. Deze zijn als volgt:
-	gaID
-	uID.

 De gaID is een nul-naar-veel relatie. De reden hiervoor is dat er geen zekerheid is dat elke game een extra iets maakt dat met de game verkocht wordt. Het kan zijn dat de extra onafhankelijk is gemaakt van een game of dat de game alleenstaand verkocht is. 
De entiteit factuur heeft twee nul-naar-veel relaties. Omdat een klant of VGHF niet perse iets moet kopen of verkopen, maar een factuur kan wel alleen maar bestaan als zowel de klant als de VGHF bestaat. 
Als laatst kan er ook waargenomen worden dat games een veel met veel relatie hebben met de klant. Voor games is dit redelijk belangrijk omdat de uitgeleende games natuurlijk ook teruggebracht moeten worden. Hiervoor moet er informatie over de klant opgeslagen worden, zodat een game aan een klant verbonden kan worden. De reden waarom dit ook een veel op veel relatie is, is omdat een klant meerdere games kan uitlenen. Ook omgekeerd zouden verschillende klanten verschillende exemplaren van dezelfde game kunnen lenen. 

## conclusie 
De database bestaat uit zes elementen met hun eigen specifieke eigenschappen zoals identificaties, informatie om de omzet te berekenen en specifieke informatie over de producten. Deze bevatten soms overlappende informatie om ze met elkaar te kunnen koppelen. Het is belangrijk om te weten dat dit uiteindelijk alleen maar een model van de database is en kan uitgebreid worden tijdens het programmeren hiervan. Hieronder kan ook nog het totale domein model schema gevonden worden. Hierbij staat 1..* voor een veel relatie, 1 voor een één relatie en 0 voor een nul relatie.  

![DDL schema](/assets/images/VGHFdatabase.drawio.png)
