package tr.com.jowl.service;

import tr.com.jowl.model.Image;

import java.util.List;

public interface ImageService {

    void save(Image image);

    Image find(Long id);

    List<Image> findAll();
}