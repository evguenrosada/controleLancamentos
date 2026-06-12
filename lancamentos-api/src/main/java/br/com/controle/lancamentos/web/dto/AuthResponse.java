package br.com.controle.lancamentos.web.dto;

import java.util.UUID;

public record AuthResponse(
        UUID comercianteId,
        String nome,
        String email,
        String accessToken
) {
}
