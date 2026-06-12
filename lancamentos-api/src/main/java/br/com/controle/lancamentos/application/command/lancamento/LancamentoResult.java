package br.com.controle.lancamentos.application.command.lancamento;

import br.com.controle.lancamentos.domain.lancamento.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record LancamentoResult(
        UUID id,
        UUID comercianteId,
        TipoLancamento tipo,
        BigDecimal valor,
        LocalDate dataReferencia,
        String descricao,
        LocalDateTime createdAt
) {
}
