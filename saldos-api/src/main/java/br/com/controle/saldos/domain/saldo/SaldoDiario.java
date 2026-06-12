package br.com.controle.saldos.domain.saldo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "saldo_diario")
public class SaldoDiario {

    @Id
    private UUID id;

    @Column(name = "comerciante_id", nullable = false)
    private UUID comercianteId;

    @Column(name = "data_referencia", nullable = false)
    private LocalDate dataReferencia;

    @Column(name = "total_creditos", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalCreditos;

    @Column(name = "total_debitos", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalDebitos;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected SaldoDiario() {
    }

    public SaldoDiario(UUID comercianteId, LocalDate dataReferencia) {
        this.id = UUID.randomUUID();
        this.comercianteId = comercianteId;
        this.dataReferencia = dataReferencia;
        this.totalCreditos = BigDecimal.ZERO;
        this.totalDebitos = BigDecimal.ZERO;
        this.saldo = BigDecimal.ZERO;
        this.updatedAt = LocalDateTime.now();
    }

    public void aplicarCredito(BigDecimal valor) {
        totalCreditos = totalCreditos.add(valor);
        recalcularSaldo();
    }

    public void aplicarDebito(BigDecimal valor) {
        totalDebitos = totalDebitos.add(valor);
        recalcularSaldo();
    }

    private void recalcularSaldo() {
        saldo = totalCreditos.subtract(totalDebitos);
        updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getComercianteId() {
        return comercianteId;
    }

    public LocalDate getDataReferencia() {
        return dataReferencia;
    }

    public BigDecimal getTotalCreditos() {
        return totalCreditos;
    }

    public BigDecimal getTotalDebitos() {
        return totalDebitos;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
