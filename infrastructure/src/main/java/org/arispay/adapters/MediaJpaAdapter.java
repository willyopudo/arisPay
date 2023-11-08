package org.arispay.adapters;

import org.arispay.data.CompanyCustomerDto;
import org.arispay.data.MediaDto;
import org.arispay.entity.CompanyCustomer;
import org.arispay.entity.Media;
import org.arispay.ports.api.GenericServicePort;
import org.arispay.repository.MediaRepository;
import org.arispay.utils.ObjMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.arispay.utils.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class MediaJpaAdapter implements GenericServicePort<MediaDto> {
	@Autowired
	MediaRepository mediaRepository;

	@Autowired
	ObjMapperUtils objMapperUtils;

	@Override
	public MediaDto add(MediaDto obj) {
		Media media = new Media();
		MediaDto isExist = findByName(obj.getName());
		if (isExist != null) {
			isExist.setUpdatedDate(obj.getCreatedDate());
			isExist.setMediaType(obj.getMediaType());
			isExist.setSize(obj.getSize());
			media = (Media) objMapperUtils.convertToEntity(new Media(), isExist);
		} else {
			media = (Media) objMapperUtils.convertToEntity(new Media(), obj);
		}
		return (MediaDto) objMapperUtils.convertToDto(mediaRepository.save(media), new MediaDto());
	}

	@Override
	public void deleteById(Long id) {
		mediaRepository.deleteById(id);
	}

	@Override
	public MediaDto update(MediaDto obj) {
		final Media media = mediaRepository.findById(obj.getId())
				.orElseThrow(NotFoundException::new);
		objMapperUtils.convertToEntity(media, obj);
		return (MediaDto) objMapperUtils.convertToDto(mediaRepository.save(media), new MediaDto());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MediaDto> getAll() {
		List<Media> mediaList = mediaRepository.findAll();

		return (List<MediaDto>) objMapperUtils.convertToDto(mediaList, new MediaDto());
	}

	@Override
	public MediaDto getById(Long id) {
		Optional<Media> media = mediaRepository.findById(id);
		if (media.isPresent()) {
			return (MediaDto) objMapperUtils.convertToDto(media, new MediaDto());
		} else
			return null;
	}


	public boolean nameExists(final String name) {
		return mediaRepository.existsByNameIgnoreCase(name);
	}


	public MediaDto findByName(String name) {
		Media media = mediaRepository.findByName(name);
		return (MediaDto) objMapperUtils.convertToDto(media, new MediaDto());

	}
}
