package org.arispay.adapters;

import org.arispay.data.MediaDto;
import org.arispay.entity.Media;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.repository.MediaRepository;
import org.arispay.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.arispay.utils.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class MediaJpaAdapter implements GenericServicePort<MediaDto> {
	@Autowired
	private MediaRepository mediaRepository;


	@Override
	public MediaDto add(MediaDto obj) {
		Media media = new Media();
		MediaDto isExist = findByName(obj.getName());
		if (isExist != null) {
			isExist.setUpdatedDate(obj.getCreatedDate());
			isExist.setMediaType(obj.getMediaType());
			isExist.setSize(obj.getSize());
			media = ObjectMapperUtils.map(isExist, Media.class);
		} else {
			media = ObjectMapperUtils.map(obj, Media.class);
		}
		return ObjectMapperUtils.map(mediaRepository.save(media), MediaDto.class);
	}

	@Override
	public void deleteById(Long id) {
		mediaRepository.deleteById(id);
	}

	@Override
	public MediaDto update(MediaDto obj) {
		final Media media = mediaRepository.findById(obj.getId())
				.orElseThrow(NotFoundException::new);
		ObjectMapperUtils.map(obj, media);
		return ObjectMapperUtils.map(mediaRepository.save(media), MediaDto.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MediaDto> getAll() {
		List<Media> mediaList = mediaRepository.findAll();

		return ObjectMapperUtils.mapAll(mediaList, MediaDto.class);
	}

	@Override
	public MediaDto getById(Long id) {
		Optional<Media> media = mediaRepository.findById(id);
		if (media.isPresent()) {
			return ObjectMapperUtils.map(media, MediaDto.class);
		} else
			return null;
	}


	public boolean nameExists(final String name) {
		return mediaRepository.existsByNameIgnoreCase(name);
	}


	public MediaDto findByName(String name) {
		Media media = mediaRepository.findByName(name);
		return ObjectMapperUtils.map(media, MediaDto.class);

	}
}
