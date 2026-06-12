package br.com.controle.lancamentos.application.command.lancamento;

import br.com.controle.lancamentos.application.command.Command;
import br.com.controle.lancamentos.domain.lancamento.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RegistrarLancamentoCommand(
        UUID comercianteId,
        TipoLancamento tipo,
        BigDecimal valor,
        LocalDate dataReferencia,
        String descricao
) implements Command<LancamentoResult> {
}
