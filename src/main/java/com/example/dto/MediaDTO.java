package com.example.dto;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MediaDTO {


	private Long id;

	@NotNull
	private String name;

	private String mediaLocation;

	private String mediaType;

	private Date createdDate;

	private Date updatedDate;

	private long size;

}
