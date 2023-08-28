package ru.workwear.server.auth.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String refreshToken;
    private String digitalSignature;
}
