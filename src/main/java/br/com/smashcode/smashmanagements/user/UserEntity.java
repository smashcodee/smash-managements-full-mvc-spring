package br.com.smashcode.smashmanagements.user;

import br.com.smashcode.smashmanagements.task.TaskEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Set;

@Entity(name = "githubuser")
@Table(name = "githubuser")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserEntity {
    @Id
    private Long id;
    private String name;
    private String avatarUrl;
    @Builder.Default
    private Integer score = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TaskEntity> tasks;

    public static UserEntity convert(OAuth2User githubuser) {
        return new UserEntityBuilder()
                .id(Long.valueOf(githubuser.getName()))
                .name(githubuser.getAttribute("name"))
                .avatarUrl(githubuser.getAttribute("avatar_url"))
                .build();
    }
}
