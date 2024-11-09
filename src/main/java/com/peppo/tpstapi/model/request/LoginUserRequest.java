package com.peppo.tpstapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserRequest {

    @NotBlank
    @Size(min = 9, max = 9)
    private String idUser;

    @NotBlank
    @Size(max = 100)
    private String password;
}
