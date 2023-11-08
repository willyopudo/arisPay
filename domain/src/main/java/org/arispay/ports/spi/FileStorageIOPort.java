package org.arispay.ports.spi;

import org.arispay.data.MediaDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FileStorageIOPort {
	MediaDto saveMedia(MultipartFile file, String fileIdentifier) throws IOException;

	Resource getMedia(String filePath) throws MalformedURLException;

	boolean deleteMedia(String filePath) throws IOException;
}
