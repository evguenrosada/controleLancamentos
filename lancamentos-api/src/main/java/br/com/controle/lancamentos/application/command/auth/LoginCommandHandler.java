package br.com.controle.lancamentos.application.command.auth;

import br.com.controle.lancamentos.application.command.CommandHandler;
import br.com.controle.lancamentos.domain.comerciante.Comerciante;
import br.com.controle.lancamentos.infrastructure.persistence.ComercianteRepository;
import br.com.controle.lancamentos.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
public class LoginCommandHandler implements CommandHandler<LoginCommand, AuthResult> {

    private final ComercianteRepository comercianteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginCommandHandler(
            ComercianteRepository comercianteRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.comercianteRepository = comercianteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResult handle(LoginCommand command) {
        Comerciante comerciante = comercianteRepository.findByEmail(command.email())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Credenciais inválidas"));

        if (!passwordEncoder.matches(command.senha(), comerciante.getSenhaHash())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Credenciais inválidas");
        }

        String token = jwtService.generateToken(comerciante.getId(), comerciante.getEmail());
        return new AuthResult(comerciante.getId(), comerciante.getNome(), comerciante.getEmail(), token);
    }
}
