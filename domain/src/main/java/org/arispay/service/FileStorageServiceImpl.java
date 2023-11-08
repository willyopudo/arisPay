package org.arispay.service;

import org.arispay.data.MediaDto;
import org.arispay.ports.api.FileStorageServicePort;
import org.arispay.ports.spi.FileStorageIOPort;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;


public class FileStorageServiceImpl implements FileStorageServicePort {


	private final FileStorageIOPort fileStorageIOPort;

	public FileStorageServiceImpl(FileStorageIOPort fileStorageIOPort) {
		this.fileStorageIOPort = fileStorageIOPort;
	}

	@Override
	public MediaDto saveMedia(MultipartFile file, String fileIdentifier) throws IOException {
		return fileStorageIOPort.saveMedia(file, fileIdentifier);
	}

	@Override
	public Resource getMedia(String filePath) throws MalformedURLException {
		return fileStorageIOPort.getMedia(filePath);
	}

	@Override
	public boolean deleteMedia(String filePath) throws IOException {
		return fileStorageIOPort.deleteMedia(filePath);
	}
}
