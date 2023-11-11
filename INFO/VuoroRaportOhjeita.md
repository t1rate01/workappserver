# Työvuoron raportoinnin endpointit ja käyttö:  
  
### Työvuoron lisäys ja päivitys - /api/report/update  
  
Method: PUT  
Odottaa Authorization tokenia, ja JSON muotoista tietoa, esimerkki:  
```json
{
    "date":"2023-11-11",
    "startTime":"09:00",
    "endTime":"17:00",
    "breaksTotal":30,
    "description":"Worked on project X"
}
```
**Päivämäärä on muodossa yyyy-MM-dd**  
  
Saa käyttäjän tokenista.  
Tarkistaa löytyykö tiedoilla päivää ja päivittää sen, jos ei löydy niin luo päivän ja täyttää sen.  
Vastaus onnistuessa tulee 200OK ja JSON:  
```json
{
  "date": "2023-11-11",
  "startTime": "09:00:00",
  "endTime": "17:00:00"
}
```
Endpoint ei hyväksy tulevaisuuden päiviä ja vastaa siihen "Can't fill future dates".  
Tarkistaa myös onko päivämäärä pyhäpäivä ja täyttää itse sen booleanin (sitä ei tule laittaa PUT mukaan  niinkuin esimerkissäkään ei ole). 

### Viimeisien 31 työvuorojen haku - /api/report/personal  
METHOD: GET  
Odottaa authorization tokenia, jonka perusteella tuo oikean henkilön vuorot.  
Palautus on JSON array muotoa esim:  
```jsonarray
[
  {
    "id": 105,
    "date": "2023-11-11",
    "startTime": "09:00:00",
    "endTime": "18:00:00",
    "breaksTotal": 30,
    "isHoliday": false,
    "description": "Worked on project X"
  },
  {
     "id": 106,
    "date": "2023-11-10",
    "startTime": "09:00:00",
    "endTime": "17:00:00",
    "breaksTotal": 30,
    "isHoliday": false,
    "description": "Worked on project X"
  },
]
```
Tulee uusin ensin järjestyksessä.  

### Custom määrä työvuoron haku - /api/report/personal/{amount}  
METHOD: GET  
Muuten samanlainen kuin aiempi, odottaa auth tokenia myös, mutta ottaa urliparametrinä myös  
määrän, montako vuoroa haluat nähdä.  Esimerkiksi /api/report/personal/5.  
**Hyvä muistaa esim kirjautuneen etusivulle!**  
  
### Viimeisimmän vuoron haku - /api/report/personal/latest  
METHOD: GET  
Jos jostain syystä tahtoo nimenomaan vain viimeisimmän vuoron, niin Tokenin kanssa täältä saa.  

  
### Vuoron poisto - /api/report/delete/{shiftID}  
METHOD: DELETE  
Odottaa tokenia + Ottaa urliparametrinä id:n, jonka saa noista Get pyynnöistä, jonka perusteella poistaa vuoron.  
Vastaa 200 OK, 400 X UNAUTHORIZED jos token ei kunnollinen, tai Shift not found jos vuoroa ei löydy.  

  


##### Olemassa myös POST endpoint työvuoroille mutta taidan jättää pois,  
##### tuo update yhdistää niin hyvin.  
