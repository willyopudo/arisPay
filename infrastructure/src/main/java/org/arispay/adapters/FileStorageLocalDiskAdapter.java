package org.arispay.adapters;

import org.arispay.data.MediaDto;
import org.arispay.ports.spi.FileStorageIOPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class FileStorageLocalDiskAdapter implements FileStorageIOPort {

	private final Path uploadPath;

	public FileStorageLocalDiskAdapter(@Value("${file.upload.path:./uploads}") String uploadPath) throws IOException {
		this.uploadPath = Paths.get(uploadPath).toAbsolutePath().normalize();
		// Create directory if it doesn't exist
		Files.createDirectories(this.uploadPath);
	}

	// Save file in disk (in project context root) and return file information in
	// media object
	@Override
	public MediaDto saveMedia(MultipartFile file, String fileName) throws IOException {

		final MediaDto media = new MediaDto();
		media.setName(fileName);
		media.setMediaType(file.getContentType() == null ? fileName.substring(fileName.lastIndexOf(".") + 1)
				: file.getContentType());

		Path targetLocation = this.uploadPath.resolve(fileName);
		media.setMediaLocation(targetLocation.toString());

		// Delete if exists
		Files.deleteIfExists(targetLocation);
		Files.copy(file.getInputStream(), targetLocation);

		media.setCreatedDate(new Date());

		return media;
	}

	// load file from the disk
	@Override
	public Resource getMedia(String filePath) throws MalformedURLException {
		Path path = Paths.get(filePath).normalize();
		return new UrlResource(path.toUri());
	}

	// Delete file from disk
	@Override
	public boolean deleteMedia(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		return Files.deleteIfExists(path);
	}
}
