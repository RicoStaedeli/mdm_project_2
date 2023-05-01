package zhaw.mdm.staedric.project2;

import java.io.InputStream;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ai.djl.ModelException;
import ai.djl.modality.cv.Image;
import ai.djl.translate.TranslateException;

@RestController
public class ObjectDetectionController {

    private ObjectDetection detection = new ObjectDetection();

    @GetMapping("/object")
    public String predict() throws Exception {
        detection.predict();
        return "Running";
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<InputStreamResource> getImage() throws IOException {
        var imgFile = new ClassPathResource("static/result.png");

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(imgFile.getInputStream()));
    }

    @PostMapping(path = "/predict")
    public ResponseEntity<byte[]> predictImage(@RequestParam("image") MultipartFile image) throws Exception {
 
        if(image == null){
            System.out.println("No image provided");
           return  new ResponseEntity<>("Failed".getBytes(), null, HttpStatus.OK);
        }
        System.out.println("Image with name: " + image.getOriginalFilename());
        Image result = detection.predictImage(image.getBytes());
      
        // convert the image to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        result.save(outputStream, "png");
        byte[] imageBytes = outputStream.toByteArray();
        
        // create a response entity with the image bytes and content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @PostMapping(path = "/analyze")
    public String predict(@RequestParam("image") MultipartFile image) throws Exception {
        detection.predictImage(image.getBytes());
        return "Okay";
    }
}
