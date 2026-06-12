package br.com.controle.lancamentos.infrastructure.persistence;

import br.com.controle.lancamentos.domain.comerciante.Comerciante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ComercianteRepository extends JpaRepository<Comerciante, UUID> {
    Optional<Comerciante> findByEmail(String email);

    boolean existsByEmail(String email);
}
