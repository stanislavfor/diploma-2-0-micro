package src.main.java.com.example.api.dto;

public class JwtResponse {
    public String token;
    public String email;
    public String role;

    public JwtResponse(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }
}
