package br.com.controle.lancamentos.application.command.auth;

import java.util.UUID;

public record AuthResult(UUID comercianteId, String nome, String email, String accessToken) {
}
