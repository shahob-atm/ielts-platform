package com.sh32bit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InviteChildRequest {
    @NotBlank(message = "childEmail can not be blank")
    private String childEmail;
}
