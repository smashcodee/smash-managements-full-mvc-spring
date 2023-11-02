package br.com.smashcode.smashmanagements.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void addScore(UserEntity githubuser, Integer score) {
        var opt = repository.findById(githubuser.getId());

        if(opt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        var user = opt.get();
        user.setScore(user.getScore() + score);
        repository.save(user);
    }
}
