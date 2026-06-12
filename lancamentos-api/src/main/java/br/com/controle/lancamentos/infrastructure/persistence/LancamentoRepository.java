package br.com.controle.lancamentos.infrastructure.persistence;

import br.com.controle.lancamentos.domain.lancamento.Lancamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LancamentoRepository extends JpaRepository<Lancamento, UUID> {
    List<Lancamento> findByComercianteIdOrderByDataReferenciaDescCreatedAtDesc(UUID comercianteId);

    List<Lancamento> findByComercianteIdAndDataReferenciaBetweenOrderByCreatedAtDesc(
            UUID comercianteId,
            LocalDate inicio,
            LocalDate fim
    );
}
