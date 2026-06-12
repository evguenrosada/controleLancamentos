package br.com.controle.lancamentos.application.command.auth;

import br.com.controle.lancamentos.application.command.Command;

public record RegisterComercianteCommand(String nome, String email, String senha) implements Command<AuthResult> {
}
