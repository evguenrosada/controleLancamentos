package br.com.controle.saldos.application.command.saldo;

import br.com.controle.saldos.application.command.CommandHandler;
import br.com.controle.saldos.domain.saldo.SaldoDiario;
import br.com.controle.saldos.infrastructure.persistence.SaldoDiarioRepository;
import org.springframework.stereotype.Component;

@Component
public class ConsolidarLancamentoCommandHandler implements CommandHandler<ConsolidarLancamentoCommand, SaldoDiarioResult> {

    private final SaldoDiarioRepository saldoDiarioRepository;

    public ConsolidarLancamentoCommandHandler(SaldoDiarioRepository saldoDiarioRepository) {
        this.saldoDiarioRepository = saldoDiarioRepository;
    }

    @Override
    public SaldoDiarioResult handle(ConsolidarLancamentoCommand command) {
        SaldoDiario saldoDiario = saldoDiarioRepository
                .findByComercianteIdAndDataReferencia(command.comercianteId(), command.dataReferencia())
                .orElseGet(() -> new SaldoDiario(command.comercianteId(), command.dataReferencia()));

        if ("CREDITO".equalsIgnoreCase(command.tipo())) {
            saldoDiario.aplicarCredito(command.valor());
        } else {
            saldoDiario.aplicarDebito(command.valor());
        }

        SaldoDiario saved = saldoDiarioRepository.save(saldoDiario);
        return toResult(saved);
    }

    static SaldoDiarioResult toResult(SaldoDiario saldoDiario) {
        return new SaldoDiarioResult(
                saldoDiario.getId(),
                saldoDiario.getComercianteId(),
                saldoDiario.getDataReferencia(),
                saldoDiario.getTotalCreditos(),
                saldoDiario.getTotalDebitos(),
                saldoDiario.getSaldo(),
                saldoDiario.getUpdatedAt()
        );
    }
}
