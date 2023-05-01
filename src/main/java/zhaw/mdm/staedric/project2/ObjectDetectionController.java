package zhaw.mdm.staedric.project2;

import java.io.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ai.djl.modality.cv.Image;

@RestController
public class ObjectDetectionController {

    private ObjectDetection detection = new ObjectDetection();

    @PostMapping(path = "/predict")
    public ResponseEntity<byte[]> predictImage(@RequestParam("image") MultipartFile image) throws Exception {

        if (image == null) {
            System.out.println("No image provided");
            return new ResponseEntity<>("Failed".getBytes(), null, HttpStatus.OK);
        }
        System.out.println("Image with name: " + image.getOriginalFilename());
        Image result = detection.predictImage(image.getBytes());

        // convert the image with the object detection to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        result.save(outputStream, "png");
        byte[] imageBytes = outputStream.toByteArray();

        // create a response entity with the image bytes and content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}
