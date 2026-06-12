package br.com.controle.lancamentos.application.command.lancamento;

import br.com.controle.lancamentos.application.command.CommandHandler;
import br.com.controle.lancamentos.domain.lancamento.Lancamento;
import br.com.controle.lancamentos.infrastructure.persistence.LancamentoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListarLancamentosCommandHandler implements CommandHandler<ListarLancamentosCommand, List<LancamentoResult>> {

    private final LancamentoRepository lancamentoRepository;

    public ListarLancamentosCommandHandler(LancamentoRepository lancamentoRepository) {
        this.lancamentoRepository = lancamentoRepository;
    }

    @Override
    public List<LancamentoResult> handle(ListarLancamentosCommand command) {
        List<Lancamento> lancamentos;

        if (command.dataInicio() != null && command.dataFim() != null) {
            lancamentos = lancamentoRepository.findByComercianteIdAndDataReferenciaBetweenOrderByCreatedAtDesc(
                    command.comercianteId(),
                    command.dataInicio(),
                    command.dataFim()
            );
        } else {
            lancamentos = lancamentoRepository.findByComercianteIdOrderByDataReferenciaDescCreatedAtDesc(
                    command.comercianteId()
            );
        }

        return lancamentos.stream()
                .map(RegistrarLancamentoCommandHandler::toResult)
                .toList();
    }
}
