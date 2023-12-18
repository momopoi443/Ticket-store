package org.example.sbdcoursework.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class ApiErrorDTO {

    private String code;

    private String description;
}
