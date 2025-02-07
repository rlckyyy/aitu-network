package aitu.network.aitunetwork.service;


import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    void setProfilePhoto(MultipartFile file);
}
