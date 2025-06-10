package com.sh32bit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InviteChildRequest {
    private String childEmail;
}
