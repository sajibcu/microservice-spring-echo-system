package com.learn.salesservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalesDto {
    private Long id;
    private ProductDto product;
    private UserDto salesTo;
    private LocalDateTime salesDate;
}
