package br.com.controle.saldos.application.command.saldo;

import br.com.controle.saldos.application.command.CommandHandler;
import br.com.controle.saldos.domain.saldo.SaldoDiario;
import br.com.controle.saldos.infrastructure.persistence.SaldoDiarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListarSaldosDiariosCommandHandler implements CommandHandler<ListarSaldosDiariosCommand, List<SaldoDiarioResult>> {

    private final SaldoDiarioRepository saldoDiarioRepository;

    public ListarSaldosDiariosCommandHandler(SaldoDiarioRepository saldoDiarioRepository) {
        this.saldoDiarioRepository = saldoDiarioRepository;
    }

    @Override
    public List<SaldoDiarioResult> handle(ListarSaldosDiariosCommand command) {
        List<SaldoDiario> saldos;

        if (command.dataInicio() != null && command.dataFim() != null) {
            saldos = saldoDiarioRepository.findByComercianteIdAndDataReferenciaBetweenOrderByDataReferenciaDesc(
                    command.comercianteId(),
                    command.dataInicio(),
                    command.dataFim()
            );
        } else {
            saldos = saldoDiarioRepository.findByComercianteIdOrderByDataReferenciaDesc(command.comercianteId());
        }

        return saldos.stream()
                .map(ConsolidarLancamentoCommandHandler::toResult)
                .toList();
    }
}
