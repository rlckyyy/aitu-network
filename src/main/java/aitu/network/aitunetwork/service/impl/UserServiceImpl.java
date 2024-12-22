package aitu.network.aitunetwork.service.impl;


import aitu.network.aitunetwork.repository.UserRepository;
import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;




}
