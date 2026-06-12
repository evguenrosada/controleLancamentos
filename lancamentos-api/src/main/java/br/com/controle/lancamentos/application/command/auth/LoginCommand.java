package br.com.controle.lancamentos.application.command.auth;

import br.com.controle.lancamentos.application.command.Command;

public record LoginCommand(String email, String senha) implements Command<AuthResult> {
}
