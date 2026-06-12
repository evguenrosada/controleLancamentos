package br.com.controle.lancamentos.application.command.auth;

import br.com.controle.lancamentos.domain.comerciante.Comerciante;
import br.com.controle.lancamentos.infrastructure.persistence.ComercianteRepository;
import br.com.controle.lancamentos.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginCommandHandlerTest {

    @Mock
    private ComercianteRepository comercianteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginCommandHandler handler;

    @Test
    void deveAutenticarComCredenciaisValidas() {
        UUID id = UUID.randomUUID();
        Comerciante comerciante = new Comerciante("João", "joao@test.com", "hash");
        when(comercianteRepository.findByEmail("joao@test.com")).thenReturn(Optional.of(comerciante));
        when(passwordEncoder.matches("123456", comerciante.getSenhaHash())).thenReturn(true);
        when(jwtService.generateToken(any(), any())).thenReturn("token-jwt");

        AuthResult result = handler.handle(new LoginCommand("joao@test.com", "123456"));

        assertEquals("token-jwt", result.accessToken());
        assertEquals("joao@test.com", result.email());
    }

    @Test
    void deveRejeitarSenhaInvalida() {
        Comerciante comerciante = new Comerciante("João", "joao@test.com", "hash");
        when(comercianteRepository.findByEmail("joao@test.com")).thenReturn(Optional.of(comerciante));
        when(passwordEncoder.matches("errada", comerciante.getSenhaHash())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> handler.handle(new LoginCommand("joao@test.com", "errada")));
    }
}
