package br.com.controle.lancamentos.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 200) String nome,
        @NotBlank @Email @Size(max = 200) String email,
        @NotBlank @Size(min = 6, max = 100) String senha
) {
}
