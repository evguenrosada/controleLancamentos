package br.com.controle.lancamentos.application.command.lancamento;

import br.com.controle.lancamentos.domain.lancamento.TipoLancamento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarLancamentoCommandHandlerTest {

    @Mock
    private br.com.controle.lancamentos.infrastructure.persistence.LancamentoRepository lancamentoRepository;

    @Mock
    private br.com.controle.lancamentos.infrastructure.redis.LancamentoEventPublisher eventPublisher;

    @InjectMocks
    private RegistrarLancamentoCommandHandler handler;

    @Test
    void deveRegistrarLancamentoEPublicarEvento() {
        UUID comercianteId = UUID.randomUUID();
        when(lancamentoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LancamentoResult result = handler.handle(new RegistrarLancamentoCommand(
                comercianteId,
                TipoLancamento.CREDITO,
                new BigDecimal("150.50"),
                LocalDate.of(2025, 6, 3),
                "Venda"
        ));

        assertEquals(TipoLancamento.CREDITO, result.tipo());
        assertEquals(new BigDecimal("150.50"), result.valor());
        verify(eventPublisher).publish(any());
    }

    @Test
    void deveRejeitarValorInvalido() {
        assertThrows(ResponseStatusException.class, () -> handler.handle(new RegistrarLancamentoCommand(
                UUID.randomUUID(),
                TipoLancamento.DEBITO,
                BigDecimal.ZERO,
                LocalDate.now(),
                null
        )));
    }
}
