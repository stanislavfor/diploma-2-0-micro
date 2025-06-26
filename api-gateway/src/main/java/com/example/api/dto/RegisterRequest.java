package src.main.java.com.example.api.dto;

public class RegisterRequest {
    public String email;
    public String password;
    public String role; // можно не передавать — по умолчанию USER
}
