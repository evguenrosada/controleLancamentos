package br.com.controle.saldos.web;

import br.com.controle.saldos.application.command.CommandBus;
import br.com.controle.saldos.application.command.saldo.ListarSaldosDiariosCommand;
import br.com.controle.saldos.application.command.saldo.SaldoDiarioResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/saldos")
public class SaldoController {

    private final CommandBus commandBus;

    public SaldoController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @GetMapping
    public List<SaldoDiarioResult> listar(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        UUID comercianteId = UUID.fromString(authentication.getName());
        return commandBus.dispatch(new ListarSaldosDiariosCommand(comercianteId, dataInicio, dataFim));
    }
}
