package com.ebp.implementation;

import com.ebp.service.imageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @Author rohit.parihar 9/15/2022
 * @Class imageImplementation
 * @Project Electricity Bill Payment
 */

@Service
public class imageImplementation implements imageService {

    @Override
    public String readingImage(String path, MultipartFile image) throws IOException {
        String imageOriginalName = image.getOriginalFilename();
        String randomName = UUID.randomUUID().toString();
        String imageRandomName = randomName.concat(imageOriginalName.substring(imageOriginalName.lastIndexOf(".")));
        String imagePath = path + File.separator + imageRandomName;
        File file = new File(path);
        if(!file.exists()){
            file.mkdir();
        }
        Files.copy(image.getInputStream(), Paths.get(imagePath));
        return imageRandomName;
    }

    @Override
    public InputStream getReadingImage(String path, String imageName) throws FileNotFoundException {
        String imagePath = path + File.separator + imageName;
        InputStream inputStream = new FileInputStream(imagePath);
        return inputStream;
    }
}
