package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public record UserFetcher(UserRepository repository) implements Function<String, User> {

    @Override
    public User apply(String userId) {
        return repository.findById(userId)
                .orElseThrow();
    }
}
