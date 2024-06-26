package tr.com.jowl.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tr.com.jowl.model.Image;
import tr.com.jowl.service.ImageService;

import java.util.List;

@Component
@Order(1)
public class ImageLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ImageLoader.class);

    @Autowired
    ImageService imageService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Loading images");
        imageService.save(new Image("Image 1"));
        imageService.save(new Image("Image 2"));
        imageService.save(new Image("Image 3"));
        imageService.save(new Image("Image 4"));
        List<Image> imageList=imageService.findAll();
        logger.info("image size {}",imageList.size() );
        imageList.forEach(image -> logger.info("img id {} name {}", image.getId(), image.getName()));
    }
}
