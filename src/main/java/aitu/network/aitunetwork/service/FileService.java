package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.service.util.GridFsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final GridFsService gridFsService;
    @Value("${secure-talk.domain}")
    private String DOMAIN;

    public String uploadFile(MultipartFile file) {
        try {
            return gridFsService.uploadFile(file);
        } catch (IOException e) {
            throw new ConflictException(e.getLocalizedMessage());
        }
    }

    public void deleteFile(String id) {
        gridFsService.deleteFile(id);
    }

    public String getLinkForResource(String hexId) {
        return DOMAIN + "/v1/file/" + hexId;
    }

    public GridFsResource getFile(String id) {
        try {
            return gridFsService.downloadFile(id);
        } catch (IOException e) {
            throw new ConflictException(e.getLocalizedMessage());
        }
    }
}
