package com.arisweb.iservices;

import java.io.IOException;
import java.net.MalformedURLException;

import com.arisweb.dto.MediaDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

	public MediaDTO saveMedia(MultipartFile file, String fileIdentifier) throws IOException;

	public Resource getMedia(String filePath) throws MalformedURLException;

	public boolean deleteMedia(String filePath) throws IOException;

}
