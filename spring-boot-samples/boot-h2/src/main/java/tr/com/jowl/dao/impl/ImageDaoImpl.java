package tr.com.jowl.dao.impl;

import org.springframework.stereotype.Repository;
import tr.com.jowl.dao.ImageDao;
import tr.com.jowl.model.Image;

@Repository
public class ImageDaoImpl extends GenericDaoImpl<Image, Long> implements ImageDao {

}