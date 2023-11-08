package org.arispay.ports.api;

import org.arispay.data.MediaDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FileStorageServicePort {
	MediaDto saveMedia(MultipartFile file, String fileIdentifier) throws IOException;

	Resource getMedia(String filePath) throws MalformedURLException;

	boolean deleteMedia(String filePath) throws IOException;
}
