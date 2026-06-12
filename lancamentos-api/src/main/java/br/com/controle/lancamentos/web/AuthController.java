package br.com.controle.lancamentos.web;

import br.com.controle.lancamentos.application.command.CommandBus;
import br.com.controle.lancamentos.application.command.auth.AuthResult;
import br.com.controle.lancamentos.application.command.auth.LoginCommand;
import br.com.controle.lancamentos.application.command.auth.RegisterComercianteCommand;
import br.com.controle.lancamentos.web.dto.AuthResponse;
import br.com.controle.lancamentos.web.dto.LoginRequest;
import br.com.controle.lancamentos.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CommandBus commandBus;

    public AuthController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        AuthResult result = commandBus.dispatch(new RegisterComercianteCommand(
                request.nome(),
                request.email(),
                request.senha()
        ));
        return toResponse(result);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = commandBus.dispatch(new LoginCommand(request.email(), request.senha()));
        return toResponse(result);
    }

    private AuthResponse toResponse(AuthResult result) {
        return new AuthResponse(result.comercianteId(), result.nome(), result.email(), result.accessToken());
    }
}
