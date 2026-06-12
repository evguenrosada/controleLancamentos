package br.com.controle.saldos.application.command;

import br.com.controle.saldos.application.command.saldo.ConsolidarLancamentoCommand;
import br.com.controle.saldos.application.command.saldo.ConsolidarLancamentoCommandHandler;
import br.com.controle.saldos.application.command.saldo.ListarSaldosDiariosCommand;
import br.com.controle.saldos.application.command.saldo.ListarSaldosDiariosCommandHandler;
import org.springframework.stereotype.Component;

@Component
public class CommandBus {

    private final ConsolidarLancamentoCommandHandler consolidarLancamentoCommandHandler;
    private final ListarSaldosDiariosCommandHandler listarSaldosDiariosCommandHandler;

    public CommandBus(
            ConsolidarLancamentoCommandHandler consolidarLancamentoCommandHandler,
            ListarSaldosDiariosCommandHandler listarSaldosDiariosCommandHandler
    ) {
        this.consolidarLancamentoCommandHandler = consolidarLancamentoCommandHandler;
        this.listarSaldosDiariosCommandHandler = listarSaldosDiariosCommandHandler;
    }

    @SuppressWarnings("unchecked")
    public <R> R dispatch(Command<R> command) {
        if (command instanceof ConsolidarLancamentoCommand consolidarLancamentoCommand) {
            return (R) consolidarLancamentoCommandHandler.handle(consolidarLancamentoCommand);
        }
        if (command instanceof ListarSaldosDiariosCommand listarSaldosDiariosCommand) {
            return (R) listarSaldosDiariosCommandHandler.handle(listarSaldosDiariosCommand);
        }
        throw new IllegalArgumentException("Command não suportado: " + command.getClass().getSimpleName());
    }
}
