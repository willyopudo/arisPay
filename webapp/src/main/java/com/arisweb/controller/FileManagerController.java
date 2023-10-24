package com.arisweb.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.arisweb.iservices.IMediaService;
import lombok.RequiredArgsConstructor;
import org.arispay.data.UserDto;
import org.arispay.ports.api.UserServicePort;
import org.arispay.repository.UserRepository;
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

import com.arisweb.dto.MediaDTO;
import com.arisweb.iservices.IFileStorageService;


@Controller
@RequestMapping("/media")
@RequiredArgsConstructor
public class FileManagerController {

	// A service to handle files storing/deleting on disk
	@Autowired
	private IFileStorageService fileStorageService;

	// a service to store file information in DB
	@Autowired
	private IMediaService mediaservice;
	@Autowired
	private final UserServicePort userServicePort;

	// show all files list
	@GetMapping
	public String list(final Model model) {
		model.addAttribute("mediaFiles", mediaservice.findAll());
		return "media/list";
	}

	// download file
	@GetMapping("/{fileName:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String fileName) throws MalformedURLException {

		final MediaDTO media = mediaservice.findByName(fileName);
		Resource file = fileStorageService.getMedia(media.getMediaLocation());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	// upload file
	@PostMapping("/upload")
	public String uploadFile(Model model, @RequestParam("mediafile") MultipartFile file, @RequestParam("fileIdentifier") String fileIdentifier) throws IOException {
		MediaDTO media = new MediaDTO();
		String userName = fileIdentifier.substring(fileIdentifier.lastIndexOf('_') + 1);
		if (!file.isEmpty()) {
			try {
				media = fileStorageService.saveMedia(file, fileIdentifier);
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

		final Long mediaId = mediaservice.create(media);
		final List<MediaDTO> mediaList = mediaservice.findAll();

		model.addAttribute("mediaFiles", mediaList);

		return "redirect:/user/profile/" + userName;
	}

	// delete file
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) throws IOException {

		MediaDTO media = mediaservice.get(id);
		fileStorageService.deleteMedia(media.getMediaLocation());
		mediaservice.delete(id);
		redirectAttributes.addFlashAttribute("MSG_INFO", "Media file deleted succesful!");
		return "redirect:/media";
	}
}
