package br.com.controle.saldos.infrastructure.persistence;

import br.com.controle.saldos.domain.saldo.SaldoDiario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SaldoDiarioRepository extends JpaRepository<SaldoDiario, UUID> {
    Optional<SaldoDiario> findByComercianteIdAndDataReferencia(UUID comercianteId, LocalDate dataReferencia);

    List<SaldoDiario> findByComercianteIdOrderByDataReferenciaDesc(UUID comercianteId);

    List<SaldoDiario> findByComercianteIdAndDataReferenciaBetweenOrderByDataReferenciaDesc(
            UUID comercianteId,
            LocalDate inicio,
            LocalDate fim
    );
}
