# workappserver
 Backend for workhours app
 # BY: TERO RANTANEN (ME)
  
# Työvuorojen määräämiseen ja työvuoroista raportointiin kehitetyn sovelluksen backend.  
  
## Käytetyt teknologiat:  
Tietokanta: PostqreSQL.  
Ohjelmointikieli: Java (Spring Boot).  

## Toimintalogiikka:  
Yritys lisätään tietokantaan, ja yrityksen omistaja/esimies lisätään esihyväksyttyjen sähköpostien listalle tietokantaan.  
Esihyväksytyllä listalla olevalla sähköpostilla pystyy rekisteröitymään. Rekisteröinnin yhteydessä käyttäjä liitetään  
esihyväksytyn sähköpostin kautta saatuun yritykseen, ja samasta hänelle määräytyy rooli. (Worker,Supervisor,Master)  
Rekisteröinnin jälkeen omistaja pystyy lisäämään esihyväksytylle listalle työntekijänsä/jäsenensä, ja heidät kaikki  
sidotaan automaattisesti hänen yritykseen/organisaatioon.  
  
Kaikille käyttäjille voidaan määrätä työvuoroja, ja kaikki voivat raportoida tehtyjä työvuoroja. Näitä säilytetään  
määriteltävissä olevan ajan tietokannassa. Roolien perusteella määräytyy, ketkä voivat muokata mitäkin tietoja  
muilta tai itseltään.  
  
## Ominaisuuksia:  
Serverillä on tietokannansiivoaja, joka ajetaan joka yö ja joka poistaa automaattisesti kaikki vanhentuneet tiedot.  
  
Serverillä on pyhäpäiväntarkistaja, joka toistaiseksi kovakoodattujen pyhäpäivien ja sunnuntain perusteella merkitsee  
raportoituihin vuoroihin pyhäpäiväbooleanin.  
  
Yrityksen Master tason käyttäjällä on mahdollisuus tallentaa yrityksensä käyttäjille näkyvän taustakuvan.  
  
Sisäänkirjautumisessa voi käyttää joko lyhyen ajan kirjautumista, jolloin saa vain normaalin accesstokenin.  
Remember me-tilassa kirjautuessaan saa myös refreshTokenin, jota käyttäessään sovellus saa uuden accesstokenin.  
Refreshtokenilla on ennaltamäärätty voimassaoloaika.  
  
Serveri kertoo sisäänkirjautumisen ja päivityksien yhteydessä frontille käyttäjän roolin, mutta rooli on myös koodattu  
tokeniin yhdessä käyttäjätunnuksen kanssa. Endpointit ja servicemethodit valvovat tarvittaessa, että käyttäjällä on oikeus  
endpointiin.  
  
Jokainen endpoint, jonne lähetetään tietoa tai jolta saadaan tietoa, lähettää ja vastaanottaa JSON muotoista tietoa.  
Tässä hyödynnetään Data Transfer Objecteja (DTO).  
  
Frontinkehitystä varten yrityksen (company) tietokantatable sisältää muiden kolumnien lisäksi myös JSON muotoista dataa  
vastaanottavan "companySettings" kolumnin, johon voi vapaassa muodossa lisätä/poistaa/päivittää dataa jota frontti hyödyntää.  
Tähän tallennetaan esimerkiksi custom taustakuvan s3 url, josta frontti saa sen ladattua.  

  
Serveri pitää tarkkaan huolen siitä, että eri yrityksien tiedot eivät missään tapauksessa kulkeudu ristiin.  
