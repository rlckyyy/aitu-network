package aitu.network.aitunetwork.service.impl;


import aitu.network.aitunetwork.repository.UserRepository;
import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void setProfilePhoto(MultipartFile file) {
//        userRepository.save()
    }
}
