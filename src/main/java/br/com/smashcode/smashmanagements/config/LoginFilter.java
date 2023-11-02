package br.com.smashcode.smashmanagements.config;

import br.com.smashcode.smashmanagements.user.UserEntity;
import br.com.smashcode.smashmanagements.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
public class LoginFilter extends OncePerRequestFilter {
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null){
            var principal = (OAuth2User) auth.getPrincipal();

            Optional<UserEntity> opt = userRepository.findById(Long.valueOf(principal.getName()));
            if (opt.isEmpty()){
                userRepository.save(UserEntity.convert(principal));
            }
        }

        filterChain.doFilter(request, response);
    }
}
