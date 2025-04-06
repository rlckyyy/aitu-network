package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {
    private final FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) throws IOException {
        GridFsResource resource = fileService.getFile(id);

        String contentType = resource.getContentType();
        if (contentType.isBlank()) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
