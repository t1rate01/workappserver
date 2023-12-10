# Taustakuvan uploadin endpointit  
  
### Taustakuvan lisäys - /api/company/backgroundimg  
  
Method: Post  
Odottaa auth tokenia, ja MultiPartFile muodossa otsikolla "image" kuva.  
Tallentaa kuvan Bucketeerin kautta luotuun S3 bucketiin, tallentaa sieltä palautuvan linkin  
tokenin käyttäjän perusteella käyttäjän companyn settingseihin nimellä "backgroundImageURL".  
Funktio tarkistaa että jokaisella companyllä on vain yksi kuva tallennettuna kerrallaan, poistaa vanhan jos uutta tulee.  
Vain master.  
Vastaus 200 OK "Image uploaded".  
  
### Taustakuvan poisto (takaisin sovelluksen defaulttiin) - /api/company/backgroundimg  
  
Method: Delete  
Odottaa auth tokenia. Vain master. Tokenista saadulta companyltä poistaa backgroundimageURL settingseistä ja  
poistaa bucketista kuvan companyn id perusteella.  
Vastaus 200 OK "Image deleted". Kertoo myös jos companyllä ei ole mitä poistaa.  
  
  
  
  
#### Muuta:  
  
Buckettiin tallentuvien kuvien etuliitteeksi (prefix) lisätään companyId, jonka perusteella funktio osaa poistaa oikean.  