package br.com.controle.lancamentos.application.command.lancamento;

import br.com.controle.lancamentos.application.command.CommandHandler;
import br.com.controle.lancamentos.domain.lancamento.Lancamento;
import br.com.controle.lancamentos.infrastructure.persistence.LancamentoRepository;
import br.com.controle.lancamentos.infrastructure.redis.LancamentoEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class RegistrarLancamentoCommandHandler implements CommandHandler<RegistrarLancamentoCommand, LancamentoResult> {

    private final LancamentoRepository lancamentoRepository;
    private final LancamentoEventPublisher eventPublisher;

    public RegistrarLancamentoCommandHandler(
            LancamentoRepository lancamentoRepository,
            LancamentoEventPublisher eventPublisher
    ) {
        this.lancamentoRepository = lancamentoRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public LancamentoResult handle(RegistrarLancamentoCommand command) {
        validate(command.valor());

        Lancamento lancamento = new Lancamento(
                command.comercianteId(),
                command.tipo(),
                command.valor(),
                command.dataReferencia(),
                command.descricao()
        );
        lancamentoRepository.save(lancamento);
        eventPublisher.publish(lancamento);

        return toResult(lancamento);
    }

    private void validate(BigDecimal valor) {
        if (valor == null || valor.signum() <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "Valor deve ser maior que zero");
        }
    }

    static LancamentoResult toResult(Lancamento lancamento) {
        return new LancamentoResult(
                lancamento.getId(),
                lancamento.getComercianteId(),
                lancamento.getTipo(),
                lancamento.getValor(),
                lancamento.getDataReferencia(),
                lancamento.getDescricao(),
                lancamento.getCreatedAt()
        );
    }
}
