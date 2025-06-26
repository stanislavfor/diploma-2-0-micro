package src.main.java.com.example.api.service;

import org.springframework.stereotype.Service;
import src.main.java.com.example.api.dto.JwtResponse;
import src.main.java.com.example.api.dto.LoginRequest;
import src.main.java.com.example.api.dto.RegisterRequest;
import src.main.java.com.example.api.repository.UserRepository;
import src.main.java.com.example.api.security.JwtTokenProvider;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder, JwtTokenProvider jwtProvider) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtProvider = jwtProvider;
    }

    public void register(RegisterRequest req) {
        if (userRepository.findByEmail(req.email).isPresent()) {
            throw new RuntimeException("Пользователь уже существует");
        }
        User user = new User();
        user.setEmail(req.email);
        user.setPassword(encoder.encode(req.password));
        user.setRole(req.role != null ? req.role : "USER");
        userRepository.save(user);
    }

    public JwtResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!encoder.matches(req.password, user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        String token = jwtProvider.generateToken(user.getEmail(), user.getRole());
        return new JwtResponse(token, user.getEmail(), user.getRole());
    }
}
