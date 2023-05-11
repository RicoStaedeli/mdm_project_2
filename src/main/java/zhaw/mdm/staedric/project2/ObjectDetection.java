package zhaw.mdm.staedric.project2;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ObjectDetection {

    private static final Logger logger = LoggerFactory.getLogger(ObjectDetection.class);
    private ZooModel<Image, DetectedObjects> model;

    public ObjectDetection() {
        String backbone;
        if ("TensorFlow".equals(Engine.getDefaultEngineName())) {
            backbone = "mobilenet_v2";
        } else {
            backbone = "resnet50";
        }
        Criteria<Image, DetectedObjects> criteria = Criteria.builder()
                .optApplication(Application.CV.OBJECT_DETECTION)
                .setTypes(Image.class, DetectedObjects.class)
                .optFilter("backbone", backbone)
                .optEngine(Engine.getDefaultEngineName())
                .optProgress(new ProgressBar())
                .build();
        logger.info("Builded criteria");
        try {
            model = criteria.loadModel();
        } catch(Exception e){
            throw new RuntimeException();
        }   
    }

    public Image predictImage(byte[] image) throws IOException, ModelException, TranslateException {
        logger.info("Start Prediction");
        //Create image Objcet out of the byte array
        InputStream is = new ByteArrayInputStream(image);
        BufferedImage bi = ImageIO.read(is);

        Image img = ImageFactory.getInstance().fromImage(bi);
        logger.info("Build Image Factory");

        //Build new Predictor to handle different requests, DJL Predictor is not MultiThread compatible
        try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
            logger.info("Predict with predictor");
            DetectedObjects detection = predictor.predict(img);
            logger.info("draw objects");
            img.drawBoundingBoxes(detection);
            return img;
        }
    }
}
