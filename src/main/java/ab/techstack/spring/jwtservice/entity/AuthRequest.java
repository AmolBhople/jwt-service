package ab.techstack.spring.jwtservice.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class AuthRequest {
    private String userName;
    private String password;
}
