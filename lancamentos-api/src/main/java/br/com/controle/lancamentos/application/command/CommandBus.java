package br.com.controle.lancamentos.application.command;

import br.com.controle.lancamentos.application.command.auth.LoginCommand;
import br.com.controle.lancamentos.application.command.auth.LoginCommandHandler;
import br.com.controle.lancamentos.application.command.auth.RegisterComercianteCommand;
import br.com.controle.lancamentos.application.command.auth.RegisterComercianteCommandHandler;
import br.com.controle.lancamentos.application.command.lancamento.ListarLancamentosCommand;
import br.com.controle.lancamentos.application.command.lancamento.ListarLancamentosCommandHandler;
import br.com.controle.lancamentos.application.command.lancamento.RegistrarLancamentoCommand;
import br.com.controle.lancamentos.application.command.lancamento.RegistrarLancamentoCommandHandler;
import org.springframework.stereotype.Component;

@Component
public class CommandBus {

    private final RegisterComercianteCommandHandler registerComercianteCommandHandler;
    private final LoginCommandHandler loginCommandHandler;
    private final RegistrarLancamentoCommandHandler registrarLancamentoCommandHandler;
    private final ListarLancamentosCommandHandler listarLancamentosCommandHandler;

    public CommandBus(
            RegisterComercianteCommandHandler registerComercianteCommandHandler,
            LoginCommandHandler loginCommandHandler,
            RegistrarLancamentoCommandHandler registrarLancamentoCommandHandler,
            ListarLancamentosCommandHandler listarLancamentosCommandHandler
    ) {
        this.registerComercianteCommandHandler = registerComercianteCommandHandler;
        this.loginCommandHandler = loginCommandHandler;
        this.registrarLancamentoCommandHandler = registrarLancamentoCommandHandler;
        this.listarLancamentosCommandHandler = listarLancamentosCommandHandler;
    }

    @SuppressWarnings("unchecked")
    public <R> R dispatch(Command<R> command) {
        if (command instanceof RegisterComercianteCommand registerComercianteCommand) {
            return (R) registerComercianteCommandHandler.handle(registerComercianteCommand);
        }
        if (command instanceof LoginCommand loginCommand) {
            return (R) loginCommandHandler.handle(loginCommand);
        }
        if (command instanceof RegistrarLancamentoCommand registrarLancamentoCommand) {
            return (R) registrarLancamentoCommandHandler.handle(registrarLancamentoCommand);
        }
        if (command instanceof ListarLancamentosCommand listarLancamentosCommand) {
            return (R) listarLancamentosCommandHandler.handle(listarLancamentosCommand);
        }
        throw new IllegalArgumentException("Command não suportado: " + command.getClass().getSimpleName());
    }
}
