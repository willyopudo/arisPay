package com.arisweb.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.arisweb.dto.MediaDTO;

@Service
public class FileStorageServiceImpl implements IFileStorageService {

	public static String MEDIA_UPLOAD_PATH = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

	// Save file in disk (in project context root) and return file information in media object
	@Override
	public MediaDTO saveMedia(MultipartFile file, String fileName) throws IOException, FileSizeLimitExceededException {

		final MediaDTO media = new MediaDTO();
		//final String fileName = fileName;//file.getOriginalFilename();
		media.setName(fileName);
		media.setMediaType(file.getContentType() == null ? fileName.substring(fileName.lastIndexOf(".") + 1) : file.getContentType());
		Path fileNameAndPath = Paths.get(MEDIA_UPLOAD_PATH);
		media.setMediaLocation(fileNameAndPath.resolve(fileName).toString());
		this.deleteMedia(fileNameAndPath.resolve(fileName).toString());
		Files.copy(file.getInputStream(), fileNameAndPath.resolve(fileName));

		media.setCreatedDate(new Date());

		return media;
	}

	// load file from the disk
	@Override
	public Resource getMedia(String filaPath) throws MalformedURLException {

		Path path = new File(filaPath).toPath();
		Resource resource = new UrlResource(path.toUri());
		return resource;
	}

	// Delete file from disk
	@Override
	public boolean deleteMedia(String filaPath) throws IOException {
		Path path = new File(filaPath).toPath();
		return Files.deleteIfExists(path);
	}
}
