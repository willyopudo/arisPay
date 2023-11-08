package com.arisweb.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.arispay.data.UserDto;
import org.arispay.ports.api.FileStorageServicePort;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.ports.api.UserServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.arispay.data.MediaDto;


@Controller
@RequestMapping("/media")
@RequiredArgsConstructor
public class FileManagerController {

	// A service to handle files storing/deleting on disk
	@Autowired
	private FileStorageServicePort fileStorageServicePort;

	// a service to store file information in DB
	@Autowired
	private GenericServicePort<MediaDto> genericServicePort;
	@Autowired
	private final UserServicePort userServicePort;

	private static final Logger logger = LogManager.getLogger(AuthController.class);

	// show all files list
	@GetMapping
	public String list(final Model model) {
		model.addAttribute("mediaFiles", genericServicePort.getAll());
		return "media/list";
	}

	// download file
	@GetMapping("/{fileName:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String fileName) throws MalformedURLException {

		final MediaDto media = genericServicePort.findByName(fileName);
		Resource file = fileStorageServicePort.getMedia(media.getMediaLocation());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	// upload file
	@PostMapping("/upload")
	public String uploadFile(Model model, @RequestParam("mediafile") MultipartFile file, @RequestParam("fileIdentifier") String fileIdentifier) throws IOException, FileSizeLimitExceededException {
		MediaDto media = new MediaDto();
		String userName = fileIdentifier.substring(fileIdentifier.lastIndexOf('_') + 1);
		if (!file.isEmpty()) {
			try {
				media = fileStorageServicePort.saveMedia(file, fileIdentifier);
				logger.debug(media.toString());
			} catch (Exception ex) {
				String fileError = ex.toString();
				if (fileError.contains("FileAlreadyExistsException:"))
					fileError = "File already exists!";
				UserDto user = userServicePort.findUserByUserName2(userName);
				model.addAttribute("user", user);
				model.addAttribute("fileError", fileError);
				return "profile";

			}
		} else
			return "redirect:/user/profile/" + userName + "?fileNull";

		MediaDto savedMedia = genericServicePort.add(media);
		final List<MediaDto> mediaList = genericServicePort.getAll();

		model.addAttribute("mediaFiles", mediaList);

		return "redirect:/user/profile/" + userName;
	}

	// delete file
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) throws IOException {

		MediaDto media = genericServicePort.getById(id);
		fileStorageServicePort.deleteMedia(media.getMediaLocation());
		genericServicePort.deleteById(id);
		redirectAttributes.addFlashAttribute("MSG_INFO", "Media file deleted successful!");
		return "redirect:/media";
	}
}
