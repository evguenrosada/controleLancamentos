package br.com.controle.lancamentos.domain.lancamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lancamentos")
public class Lancamento {

    @Id
    private UUID id;

    @Column(name = "comerciante_id", nullable = false)
    private UUID comercianteId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoLancamento tipo;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_referencia", nullable = false)
    private LocalDate dataReferencia;

    @Column(length = 500)
    private String descricao;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Lancamento() {
    }

    public Lancamento(
            UUID comercianteId,
            TipoLancamento tipo,
            BigDecimal valor,
            LocalDate dataReferencia,
            String descricao
    ) {
        this.id = UUID.randomUUID();
        this.comercianteId = comercianteId;
        this.tipo = tipo;
        this.valor = valor;
        this.dataReferencia = dataReferencia;
        this.descricao = descricao;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getComercianteId() {
        return comercianteId;
    }

    public TipoLancamento getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataReferencia() {
        return dataReferencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
