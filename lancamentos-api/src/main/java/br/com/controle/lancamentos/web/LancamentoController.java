package br.com.controle.lancamentos.web;

import br.com.controle.lancamentos.application.command.CommandBus;
import br.com.controle.lancamentos.application.command.lancamento.LancamentoResult;
import br.com.controle.lancamentos.application.command.lancamento.ListarLancamentosCommand;
import br.com.controle.lancamentos.application.command.lancamento.RegistrarLancamentoCommand;
import br.com.controle.lancamentos.web.dto.LancamentoRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

    private final CommandBus commandBus;

    public LancamentoController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LancamentoResult registrar(@Valid @RequestBody LancamentoRequest request, Authentication authentication) {
        UUID comercianteId = UUID.fromString(authentication.getName());
        return commandBus.dispatch(new RegistrarLancamentoCommand(
                comercianteId,
                request.tipo(),
                request.valor(),
                request.dataReferencia(),
                request.descricao()
        ));
    }

    @GetMapping
    public List<LancamentoResult> listar(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        UUID comercianteId = UUID.fromString(authentication.getName());
        return commandBus.dispatch(new ListarLancamentosCommand(comercianteId, dataInicio, dataFim));
    }
}
