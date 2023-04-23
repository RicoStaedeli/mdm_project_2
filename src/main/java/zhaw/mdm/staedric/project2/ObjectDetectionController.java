package zhaw.mdm.staedric.project2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ai.djl.modality.cv.Image;

@RestController
public class ObjectDetectionController {

    private ObjectDetection detection = new ObjectDetection();

    @GetMapping("/object")
    public String predict() throws Exception {
        detection.predict();
        return "Running";
    }

    @PostMapping(path = "/analyze")
    public String predict(@RequestParam("image") MultipartFile image) throws Exception {
        detection.predictImage(image.getBytes());
        return "Hallo";
    }
}
