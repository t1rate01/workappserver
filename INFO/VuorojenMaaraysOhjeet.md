# Työvuorojen määräys endpoint ohjeet  
  
### Työvuoron lisäys ja päivitys - /api/shifts/update  
  
Method: Put  
Odottaa Authorization tokenia, ja JSON muotoista tietoa, jossa vähintään:  
```json
{
    "id":"*SEN USERIN ID KENELLE MÄÄRÄTÄÄN*",
    "date":"2023-11-11",
    "startTime":"09:00:00",
    "endTime":"17:00:00",   end time myös vapaaehtoinen
}

vapaaehtoiset:
"description":"Lakase liat ja lahtaa siat",
"breaksTotal":"45",  jos haluaa ennaltamäärätä taukomäärän
```
  
Saa käyttäjän ja roolin tokenista, tarkistaa että vähintään Supervisor/Master.  
Samalla lailla kuin vuoron raportoinnissa, tekee uuden tai päivittää olemassaolevan tarpeen mukaan.  
Vastaus 200OK ja JSON start time ja end time ja date.  

### Työvuoron poistaminen   - /api/shifts/delete/{shiftId}
  
Method: Delete  
Odottaa auth tokenia ja urliparametrinä poistettavan vuoron Id.  
Tarkistaa tokenista käyttöoikeuden ja poistaa vuoron jos menee läpi.  
Onnistuessa 200 OK vastaus.  
  
### Minulle määrätyt työvuorot - /api/shifts/personal  
  
Method: Get  
Odottaa auth tokenia, josta saa tarvittavat tiedot.  
Vastauksena tulee lista käyttäjälle määrätyistä vuoroista, jotka tänään tai tulevaisuudessa.    
  
### Minulle määrätyt työvuorot - /api/shifts/personal/all  
  
Method: Get  
Kuin edellinen mutta antaa myös menneet vuorot.  
  
### Companyn kaikkien työntekijöiden vuorot - /api/shifts/everyone  
  
Method: Get  
Odottaa auth tokenia, josta saa tarvittavat tiedot ja tarkistaa roolin.  
Palauttaa kaikki companyssa määrätyt vuorot, jotka tänään tai tulevaisuudessa.  
  
### Companyn kaikkien työntekijöiden vuorot - /api/shifts/everyone/all  
  
Method: Get  
Kuin edellinen mutta palauttaa myös menneet vuorot.  
  


