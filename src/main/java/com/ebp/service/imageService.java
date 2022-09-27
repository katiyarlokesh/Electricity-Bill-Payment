package com.ebp.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author rohit.parihar 9/15/2022
 * @Class imageService
 * @Project Electricity Bill Payment
 */
public interface imageService {
    String readingImage(String path, MultipartFile image) throws IOException;
    InputStream getReadingImage(String path, String imageName) throws FileNotFoundException;
}
