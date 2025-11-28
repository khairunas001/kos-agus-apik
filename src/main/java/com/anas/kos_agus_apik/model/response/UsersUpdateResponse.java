package com.anas.kos_agus_apik.model.response;

import com.anas.kos_agus_apik.entity.enum_class.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsersUpdateResponse {

    @Size(max = 100)
    private String id;

    @Size(max = 100)
    private String username;

    @Size(max = 100)
    private String password;

    @Size(max = 100)
    private String name;

    @Size(max = 20)
    private String phone;

    @Email
    @Size(max = 100)
    private String email;

    private Role roles;

}
