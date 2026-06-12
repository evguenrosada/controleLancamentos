package br.com.controle.saldos.application.command.saldo;

import br.com.controle.saldos.application.command.Command;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ListarSaldosDiariosCommand(
        UUID comercianteId,
        LocalDate dataInicio,
        LocalDate dataFim
) implements Command<List<SaldoDiarioResult>> {
}
