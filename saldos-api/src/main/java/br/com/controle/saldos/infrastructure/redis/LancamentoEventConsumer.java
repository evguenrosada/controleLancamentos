package br.com.controle.saldos.infrastructure.redis;

import br.com.controle.saldos.application.command.CommandBus;
import br.com.controle.saldos.application.command.saldo.ConsolidarLancamentoCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class LancamentoEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(LancamentoEventConsumer.class);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CommandBus commandBus;
    private final String queueName;

    public LancamentoEventConsumer(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            CommandBus commandBus,
            @Value("${app.redis.lancamento-events-queue}") String queueName
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.commandBus = commandBus;
        this.queueName = queueName;
    }

    @Scheduled(fixedDelay = 2000)
    public void consumirEventos() {
        String payload;
        while ((payload = redisTemplate.opsForList().leftPop(queueName)) != null) {
            processarPayload(payload);
        }
    }

    private void processarPayload(String payload) {
        try {
            LancamentoRegistradoEvent event = objectMapper.readValue(payload, LancamentoRegistradoEvent.class);
            commandBus.dispatch(new ConsolidarLancamentoCommand(
                    event.lancamentoId(),
                    event.comercianteId(),
                    event.tipo(),
                    event.valor(),
                    event.dataReferencia()
            ));
        } catch (Exception exception) {
            log.error("Falha ao processar evento de lançamento: {}", payload, exception);
        }
    }

    public record LancamentoRegistradoEvent(
            UUID lancamentoId,
            UUID comercianteId,
            String tipo,
            BigDecimal valor,
            LocalDate dataReferencia
    ) {
    }
}
