package com.backend.server.utility;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        public String uploadImage(MultipartFile imageFile) throws IOException {
            String fileName = generateFileName(imageFile.getOriginalFilename());
            File file = convertMultiPartToFile(imageFile);
            s3client.putObject(bucketName, fileName, file);
            file.delete();

            return s3client.getUrl(bucketName, fileName).toExternalForm();  // form jotta url pysyy varmasti urlistringinä
        }

        private File convertMultiPartToFile(MultipartFile file)  throws IOException {
            File convFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        }

        private String generateFileName(String originalFileName) {
            return UUID.randomUUID().toString() + "_" + originalFileName.replace(" ", "_");
        }

}
