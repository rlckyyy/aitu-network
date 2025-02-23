package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.service.util.GridFsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final GridFsService gridFsService;

    public String uploadPhoto(MultipartFile file) {
        try {
            return gridFsService.uploadFile(file);
        } catch (IOException e) {
            throw new ConflictException(e.getLocalizedMessage());
        }
    }
    public void deleteFile(String id){
        gridFsService.deleteFile(id);
    }

    public InputStreamResource getFile(String id) {
        try {
            return gridFsService.downloadFile(id);
        } catch (IOException e) {
            throw new ConflictException(e.getLocalizedMessage());
        }
    }
}
