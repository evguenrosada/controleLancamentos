package br.com.controle.lancamentos.application.command.lancamento;

import br.com.controle.lancamentos.application.command.Command;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ListarLancamentosCommand(
        UUID comercianteId,
        LocalDate dataInicio,
        LocalDate dataFim
) implements Command<List<LancamentoResult>> {
}
