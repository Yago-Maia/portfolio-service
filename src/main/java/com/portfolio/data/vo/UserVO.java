package com.portfolio.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    @ApiModelProperty(notes = "Identificação do usuário", required = true)
    private Long id;

    @ApiModelProperty(notes = "Primeiro nome do usuário", required = true)
    @NotEmpty(message = "Campo firstName obrigatório.")
    private String firstName;

    @ApiModelProperty(notes = "Último nome do usuário", required = true)
    @NotEmpty(message = "Campo lastName obrigatório.")
    private String lastName;

    @ApiModelProperty(notes = "E-mail do usuário", required = true)

    @NotEmpty(message = "Campo email obrigatório.")
    private String email;

    @ApiModelProperty(notes = "Senha do usuário", required = true)
    @NotEmpty(message = "Campo password obrigatório.")
    private String password;

    @ApiModelProperty(notes = "Role do usuário")
    private RoleVO role;
}
