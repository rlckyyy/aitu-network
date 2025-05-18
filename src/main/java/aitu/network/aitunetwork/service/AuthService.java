package aitu.network.aitunetwork.service;


import aitu.network.aitunetwork.common.exception.BadRequestException;
import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.GoneException;
import aitu.network.aitunetwork.common.exception.NotFoundException;
import aitu.network.aitunetwork.common.exception.UnauthorizedException;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.config.security.CustomUserDetailsService;
import aitu.network.aitunetwork.config.security.JwtService;
import aitu.network.aitunetwork.model.dto.JwtResponse;
import aitu.network.aitunetwork.model.dto.LoginRequest;
import aitu.network.aitunetwork.model.dto.RegisterRequest;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.enums.Role;
import aitu.network.aitunetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final Executor executor;
    private final MailService mailService;

    @Value("${secure-talk.mail-confirmation}")
    private Boolean isEmailConfirmationEnabled;

    public CompletableFuture<User> registerUser(RegisterRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                User user = mapUserDTOToUser(request);
                if (isEmailConfirmationEnabled) {
                    sendVerificationMessage(user);
                }
                return userRepository.save(user);
            } catch (DuplicateKeyException e) {
                throw new ConflictException("errors.409.users.email");
            }
        }, executor).exceptionally(e -> {
            throw new RuntimeException("Error while register of user: " + e.getMessage(), e);
        });
    }

    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));
        var user = (CustomUserDetails) customUserDetailsService.
                loadUserByUsername(request.email());
        if (!user.user().isEnabled()) {
            throw new UnauthorizedException("Account is not confirmed");
        }
        var jwt = jwtService.generateToken(user);
        log.info("User {} logged in", request.email());
        return new JwtResponse(jwt);
    }

    public void confirmAccount(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() ->
                        new NotFoundException(String.format("%s user with this token not found", token)));
        if (user.isEnabled()) {
            throw new GoneException("User is already enabled");
        }

        if (Objects.isNull(user.getTokenExpiryDate())) {
            throw new ConflictException("Token expiry date is null");
        }

        if (LocalDateTime.now().isAfter(user.getTokenExpiryDate())) {
            throw new BadRequestException("Token expired");
        }
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void resendConfirmationToken(String username) {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() ->
                        new NotFoundException(User.class, User.Fields.email, username));
        user.setTokenExpiryDate(LocalDateTime.now().plusHours(24));
        user.setVerificationToken(UUID.randomUUID().toString());
        userRepository.save(user);
        sendVerificationMessage(user);
    }

    private User mapUserDTOToUser(RegisterRequest userDTO) {
        return User.builder()
                .email(userDTO.email())
                .username(userDTO.username())
                .password(passwordEncoder.encode(userDTO.password()))
                .roles(List.of(Role.USER))
                .enabled(!isEmailConfirmationEnabled)
                .verificationToken(UUID.randomUUID().toString())
                .tokenExpiryDate(LocalDateTime.now().plusHours(24))
                .friendList(new ArrayList<>())
                .build();
    }

    private void sendVerificationMessage(User user) {
        CompletableFuture.runAsync(
                () -> mailService.sendConfirmationMessage(user),
                executor);
    }
}
