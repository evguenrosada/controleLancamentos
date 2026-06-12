package br.com.controle.saldos.application.command.saldo;

import br.com.controle.saldos.domain.saldo.SaldoDiario;
import br.com.controle.saldos.infrastructure.persistence.SaldoDiarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsolidarLancamentoCommandHandlerTest {

    @Mock
    private SaldoDiarioRepository saldoDiarioRepository;

    @InjectMocks
    private ConsolidarLancamentoCommandHandler handler;

    @Test
    void deveConsolidarCreditoEmSaldoExistente() {
        UUID comercianteId = UUID.randomUUID();
        LocalDate data = LocalDate.of(2025, 6, 3);
        SaldoDiario saldo = new SaldoDiario(comercianteId, data);
        saldo.aplicarCredito(new BigDecimal("100.00"));

        when(saldoDiarioRepository.findByComercianteIdAndDataReferencia(comercianteId, data))
                .thenReturn(Optional.of(saldo));
        when(saldoDiarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SaldoDiarioResult result = handler.handle(new ConsolidarLancamentoCommand(
                UUID.randomUUID(),
                comercianteId,
                "CREDITO",
                new BigDecimal("50.00"),
                data
        ));

        assertEquals(new BigDecimal("150.00"), result.totalCreditos());
        assertEquals(new BigDecimal("150.00"), result.saldo());
    }

    @Test
    void deveCriarSaldoQuandoNaoExistir() {
        UUID comercianteId = UUID.randomUUID();
        LocalDate data = LocalDate.of(2025, 6, 3);

        when(saldoDiarioRepository.findByComercianteIdAndDataReferencia(comercianteId, data))
                .thenReturn(Optional.empty());
        when(saldoDiarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SaldoDiarioResult result = handler.handle(new ConsolidarLancamentoCommand(
                UUID.randomUUID(),
                comercianteId,
                "DEBITO",
                new BigDecimal("20.00"),
                data
        ));

        assertEquals(new BigDecimal("20.00"), result.totalDebitos());
        assertEquals(new BigDecimal("-20.00"), result.saldo());
    }
}
