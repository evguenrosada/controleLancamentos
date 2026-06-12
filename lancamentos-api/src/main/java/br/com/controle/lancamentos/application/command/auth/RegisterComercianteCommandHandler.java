package br.com.controle.lancamentos.application.command.auth;

import br.com.controle.lancamentos.application.command.CommandHandler;
import br.com.controle.lancamentos.domain.comerciante.Comerciante;
import br.com.controle.lancamentos.infrastructure.persistence.ComercianteRepository;
import br.com.controle.lancamentos.infrastructure.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;

@Component
public class RegisterComercianteCommandHandler implements CommandHandler<RegisterComercianteCommand, AuthResult> {

    private final ComercianteRepository comercianteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public RegisterComercianteCommandHandler(
            ComercianteRepository comercianteRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.comercianteRepository = comercianteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResult handle(RegisterComercianteCommand command) {
        if (comercianteRepository.existsByEmail(command.email())) {
            throw new ResponseStatusException(CONFLICT, "E-mail já cadastrado");
        }

        Comerciante comerciante = new Comerciante(
                command.nome(),
                command.email(),
                passwordEncoder.encode(command.senha())
        );
        comercianteRepository.save(comerciante);

        String token = jwtService.generateToken(comerciante.getId(), comerciante.getEmail());
        return new AuthResult(comerciante.getId(), comerciante.getNome(), comerciante.getEmail(), token);
    }
}
