package com.pnu.nonata.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class UserDto {
    private String kakao_QR_url;

    private String nick_name;

    private String profile_image;

}
