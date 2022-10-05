package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "Jwt Request")
@Getter
@Setter
@AllArgsConstructor
public class JwtRequestDto {

    @Schema(title = "email",
            description = "Валидный email")
    private String email;
    @Schema(title = "password",
            description = """
                    Латиница, цифры, точка, тире, подчеркивание.
                    От 6 до 20 символов.
                    [a-zA-Z\\d._\\-]{6,20}
                    """)
    private String password;

}
