package tr.com.jowl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.jowl.dao.ImageDao;
import tr.com.jowl.model.Image;

import java.util.List;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageDao imageDao;

    @Override
    public void save(Image image) {
        imageDao.save(image);
    }

    @Override
    public Image find(Long id) {
        return imageDao.find(Image.class, id);
    }

    @Override
    public List<Image> findAll() {
        return imageDao.findAll(Image.class);
    }
}
