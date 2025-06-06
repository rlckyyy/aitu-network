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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final GridFsService gridFsService;
    @Value("${secure-talk.file-download-path}")
    private String path;

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
        return path + hexId;
    }

    public GridFsResource getFile(String id) {
        return gridFsService.downloadFile(id);
    }

    public void deleteFilesByLink(List<String> mediaFileIds) {
        //["https://aitunet.kz/api/v1/file/68172d4e1391893c7f5279ea"]
        mediaFileIds.stream()
                .map(link -> link.substring(link.lastIndexOf('/') + 1)).
                forEach(this::deleteFile);
    }
}
