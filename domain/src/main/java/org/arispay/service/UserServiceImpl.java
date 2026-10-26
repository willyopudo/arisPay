package org.arispay.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.data.MediaDto;
import org.arispay.data.UserDto;
import org.arispay.data.UserFilterDto;
import org.arispay.data.UserImageDto;
import org.arispay.ports.api.FileStorageServicePort;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.ports.api.UserServicePort;
import org.arispay.ports.spi.UserPersistencePort;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserServicePort {

	private final UserPersistencePort userPersistencePort;
	private FileStorageServicePort fileStorageServicePort;
	private GenericServicePort<MediaDto> mediaServicePort;
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

	public UserServiceImpl(UserPersistencePort userPersistencePort) {
		this.userPersistencePort = userPersistencePort;
	}

	public UserServiceImpl(UserPersistencePort userPersistencePort,
	                       FileStorageServicePort fileStorageServicePort,
	                       GenericServicePort<MediaDto> mediaServicePort) {
		this.userPersistencePort = userPersistencePort;
		this.fileStorageServicePort = fileStorageServicePort;
		this.mediaServicePort = mediaServicePort;
	}

	@Override
	public UserDto saveUser(UserDto userDto) {
		return userPersistencePort.saveUser(userDto);
	}

	@Override
	public UserDto findUserByEmail(String email) {
		return userPersistencePort.findUserByEmail(email);
	}

	@Override
	public UserDto findUserById(int id) {
		return userPersistencePort.findUserById(id);
	}

	@Override
	public UserDto findUserByUsername(String username) {
		return userPersistencePort.findUserByUserName(username);
	}

	@Override
	public UserDto findUserByUserName2(String username) {

		return userPersistencePort.findUserByUserName2(username);
	}


	public void deleteUserById(Long id) {
		userPersistencePort.deleteUserById(id);
	}

	@Override
	public Page<UserDto> findAllUsers(Pageable pageable, UserFilterDto filterDto) {
		Page<UserDto> users = userPersistencePort.findAllUsers(pageable, filterDto);
//		return users.stream()
//				.map(this::mapToUserDto)
//				.collect(Collectors.toList());
		return users;
	}

	@Override
	public UserDto findUserByToken(String token) {
		return userPersistencePort.findUserByToken(token);
	}

	@Override
	public UserDto setPassword(String token, String password) {
		return userPersistencePort.setPassword(token, password);
	}

	@Override
	public void deleteUserCompanyById(Long userId, Long companyId) {
		userPersistencePort.deleteUserCompanyById(userId, companyId);
	}

	@Override
	public UserDto uploadProfilePicture(Long userId, MultipartFile file) throws IOException {
		// Get the user
		UserDto userDto = userPersistencePort.findUserById(Math.toIntExact(userId));
		if (userDto == null) {
			throw new IllegalArgumentException("User not found with id: " + userId);
		}

		// Generate unique filename for the user's profile picture
		String originalFilename = file.getOriginalFilename();
		String fileExtension = originalFilename != null && originalFilename.contains(".")
			? originalFilename.substring(originalFilename.lastIndexOf("."))
			: ".jpg";
		String fileName = "profile_" + userId + "_" + System.currentTimeMillis() + fileExtension;

		// Save file to disk using FileStorageLocalDiskAdapter
		MediaDto mediaDto = fileStorageServicePort.saveMedia(file, fileName);
		mediaDto.setSize(file.getSize());

		// Save media metadata to database using MediaJpaAdapter
		MediaDto savedMedia = mediaServicePort.add(mediaDto);

		// Delete old profile picture if exists
		if (userDto.getImageId() != null) {
			logger.debug("User {} already has a profile picture with media ID {}. Deleting old picture.", userId, userDto.getImageId());
			try {
				MediaDto oldMedia = mediaServicePort.getById(userDto.getImageId());
				if (oldMedia != null) {
					fileStorageServicePort.deleteMedia(oldMedia.getMediaLocation());
					mediaServicePort.deleteById(oldMedia.getId());
				}
			} catch (Exception e) {
				// Log but don't fail if old image deletion fails
				System.err.println("Failed to delete old profile picture: " + e.getMessage());
			}
		}

		// Update user with new image reference
		userDto.setImageId(savedMedia.getId());
		return userPersistencePort.saveUser(userDto);
	}

	@Override
	public UserImageDto getUserImage(Long userId) throws IOException {
		// Get the user
		UserDto userDto = userPersistencePort.findUserById(Math.toIntExact(userId));
		if (userDto == null) {
			throw new IllegalArgumentException("User not found with id: " + userId);
		}

		// Check if user has a profile picture
		if (userDto.getImageId() == null) {
			return null;
		}

		// Get media metadata
		MediaDto mediaDto = mediaServicePort.getById(userDto.getImageId());
		if (mediaDto == null) {
			return null;
		}

		// Load image file from disk
		Resource imageResource = fileStorageServicePort.getMedia(mediaDto.getMediaLocation());
		byte[] imageBytes = Files.readAllBytes(imageResource.getFile().toPath());
		String base64Image = Base64.getEncoder().encodeToString(imageBytes);

		// Build response DTO
		return UserImageDto.builder()
			.mediaId(mediaDto.getId())
			.name(mediaDto.getName())
			.mediaType(mediaDto.getMediaType())
			.createdDate(mediaDto.getCreatedDate())
			.size(mediaDto.getSize())
			.base64Image(base64Image)
			.build();
	}


}
