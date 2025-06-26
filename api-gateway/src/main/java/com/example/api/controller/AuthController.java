package src.main.java.com.example.api.controller;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
        service.register(req);
        return ResponseEntity.ok("Регистрация успешна");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(service.login(req));
    }
}
