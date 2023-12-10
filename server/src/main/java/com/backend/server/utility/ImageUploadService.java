package com.backend.server.utility;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.AmazonS3;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Service
public class ImageUploadService {  
     // Käytetään custom backgroundin tallentamiseen. Kuvat tallennetaan bucketeerin kautta, ja kuvan url tallennetaan companyn settingseihin
     // ohjeet saatu tutorialista.
        private AmazonS3 s3client;

        @Autowired
        public ImageUploadService(AmazonS3 s3client) {
            this.s3client = s3client;
        }
        
        @Value("${aws.bucket-name}")
        private String bucketName;

        public void deleteImage(Long companyID) {
            deleteExistingFileForCompany(companyID, true);
        }

        
        public String uploadImage(MultipartFile imageFile, Long companyID) throws IOException {
            String fileName = "public/" + generateFileName(imageFile.getOriginalFilename(), companyID);
            // tarkista onko companylla jo kuva, jos on niin poista
            deleteExistingFileForCompany(companyID, false);

            File file = convertMultiPartToFile(imageFile);

            // Uusi request objecti, permissions public read
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            // upload
            s3client.putObject(putObjectRequest);
            file.delete();

            return s3client.getUrl(bucketName, fileName).toExternalForm();
        }

        private File convertMultiPartToFile(MultipartFile file)  throws IOException {
            File convFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        }

        private String generateFileName(String originalFileName, Long companyID) {
            String companyIDString = companyID.toString();
            return companyIDString + "_" +UUID.randomUUID().toString() + "_" + originalFileName.replace(" ", "_");
        }

        private void deleteExistingFileForCompany(Long companyId, Boolean publicCall) {   // public call niin kutsu tulee esim deletemappingistä, jonne hyvä kertoa jos mitään ei löydy
        String companyPrefix = "public/"+ companyId + "_";

        // Listaa kaikki tiedostot bucketista, katso tiedostojen alut, jos alkaa companyID:llä, poista
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(companyPrefix);
        List<S3ObjectSummary> objectSummaries = s3client.listObjects(listObjectsRequest).getObjectSummaries();

        // jos publicCall, ja ei löydy tiedostoja, heitä virhe
        if(publicCall && objectSummaries.size() == 0) {
            throw new RuntimeException("No image to delete found for company: " + companyId);
        }

        // Poista tiedostot jotka alkaa companyID:llä
        for (S3ObjectSummary os : objectSummaries) {
            s3client.deleteObject(new DeleteObjectRequest(bucketName, os.getKey()));
        }
}


}
