package br.com.controle.lancamentos.infrastructure.redis;

import br.com.controle.lancamentos.domain.lancamento.Lancamento;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class LancamentoEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(LancamentoEventPublisher.class);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final String queueName;

    public LancamentoEventPublisher(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            @Value("${app.redis.lancamento-events-queue}") String queueName
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.queueName = queueName;
    }

    public void publish(Lancamento lancamento) {
        LancamentoRegistradoEvent event = new LancamentoRegistradoEvent(
                lancamento.getId(),
                lancamento.getComercianteId(),
                lancamento.getTipo().name(),
                lancamento.getValor(),
                lancamento.getDataReferencia()
        );

        try {
            String payload = objectMapper.writeValueAsString(event);
            redisTemplate.opsForList().rightPush(queueName, payload);
        } catch (JsonProcessingException exception) {
            log.error("Falha ao publicar evento de lançamento {}", lancamento.getId(), exception);
        } catch (Exception exception) {
            log.warn(
                    "Redis indisponível — lançamento {} persistido, consolidação será eventual",
                    lancamento.getId(),
                    exception
            );
        }
    }

    public record LancamentoRegistradoEvent(
            java.util.UUID lancamentoId,
            java.util.UUID comercianteId,
            String tipo,
            java.math.BigDecimal valor,
            java.time.LocalDate dataReferencia
    ) {
    }
}
