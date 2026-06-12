package br.com.controle.saldos.application.command.saldo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record SaldoDiarioResult(
        UUID id,
        UUID comercianteId,
        LocalDate dataReferencia,
        BigDecimal totalCreditos,
        BigDecimal totalDebitos,
        BigDecimal saldo,
        LocalDateTime updatedAt
) {
}
