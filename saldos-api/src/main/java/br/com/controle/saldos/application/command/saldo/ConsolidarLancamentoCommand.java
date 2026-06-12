package br.com.controle.saldos.application.command.saldo;

import br.com.controle.saldos.application.command.Command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ConsolidarLancamentoCommand(
        UUID lancamentoId,
        UUID comercianteId,
        String tipo,
        BigDecimal valor,
        LocalDate dataReferencia
) implements Command<SaldoDiarioResult> {
}
