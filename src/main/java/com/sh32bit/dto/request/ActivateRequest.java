package com.sh32bit.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivateRequest {
    private String token;
    private String password;
}
