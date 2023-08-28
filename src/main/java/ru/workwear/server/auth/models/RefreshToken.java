package ru.workwear.server.auth.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table(name = "Refresh_Tokens")
public class RefreshToken {
    @Id
    private Long id;

    private String token;
    private String newToken;
}
