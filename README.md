# workappserver
 Backend for workhours app



Toteutuksia ja mietteitä:  

Loginissa saadaan 2 tokenia, 1 lyhyt ja 1 pitkäkestonen, lyhytkestosella pääsee toimintoihin.  
Pitkäkestonen tallennetaan laitteelle ja tietokantaan, ja jos frontilla epäonnistuu login lyhyellä  
tokenilla (on vanhentunut), frontti saa 401 vastauksen, jolloin frontin täytyy yrittää refreshata  
token lähettämällä pitkäkestonen token, jonka servu autentikoi ja onnistuessa lähettää uuden tokenin. 
Vaihtoehtoisesti frontin vois apin avatessa aina laittaa refresh token.   
Ei tarvitse laittaa kirjautumaan uudestaan eikä tarvitse pitää pitkäikäistä accesstokenia.  
Tähän jäi miettimättä isommin että mitä jos "Stay logged in" oliskin vaihtoehto...
  
Kolme roolitasoa users: WORKER, SUPERVISOR, MASTER.  
Tarkoitus että Worker näkee ja editoi vain omat tietonsa.  
Supervisor voi antaa vuoroja ja nähdä muiden tietoja.  
Master em. lisäksi lisätä approved emails.  
Login palauttaa LoginResponse olion, jossa mukana role. Role perusteella frontin täytyy  
valita menunäkymä ja mitä tietoja hakee. Role on myös enkoodattu tokeniin, niin väärää tietoa ei tule.  
  
ApprovedEmails listaan verrataan kun yritetään rekisteröityä (tapahtuu email ja salasana).  
Listalta löytyessä kytketään työntekijä sitten sillä tiedolla suoraan oikeaan companyyn.  
Emailit aina uniikkeja ja toimii käyttäjänimen sijasta, ei erillistä käyttäjänimeä.  

Alla suunnitelmaa, huutomerkillä merkatut on tekemättä tai isosti kesken.  
Käytetty Lombok-kirjastoa kontstruktoreiden, getterien ja setterien generointiin  
jotta koodi pysyy luettavampana.  
  

Database ~~MySQL~~ PostGreSQL, logiikka:  
Company, jossa onetomany suhde Users ja CompanyApprovedEmails  
Users, jolla manytoone suhde Company, ja TODO merkattu shifts ja työaikaraportit  
Shifts, joilla manytoone suhde Users. TODO  
ReportedHours, joilla manytoone suhde Users. TODO  

RestControllereissa huomioitava että securityn funktiot ei palauta NULL epäonnistuessa,  
vaan IllegalArgumentException. Restissä täytyy käyttää "catch IllegalArgumentException e"  
kun pitää saada kiinni loginin epäonnistumisesta.    



