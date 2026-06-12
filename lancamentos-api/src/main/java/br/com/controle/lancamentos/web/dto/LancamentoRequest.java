package br.com.controle.lancamentos.web.dto;

import br.com.controle.lancamentos.domain.lancamento.TipoLancamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequest(
        @NotNull TipoLancamento tipo,
        @NotNull @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero") BigDecimal valor,
        @NotNull LocalDate dataReferencia,
        @Size(max = 500) String descricao
) {
}
