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
public class UserImageDto {
    private Long mediaId;
    private String name;
    private String mediaType;
    private Date createdDate;
    private long size;
    private String base64Image;
}
