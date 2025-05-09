package com.learn.salesservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
