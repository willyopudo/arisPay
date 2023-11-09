package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaDto {
	private Long id;

	private String name;

	private String mediaLocation;

	private String mediaType;

	private Date createdDate;

	private Date updatedDate;

	private long size;
}
