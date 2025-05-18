package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {
    private final FileService fileService;

    @GetMapping("/{id}")
    CompletableFuture<ResponseEntity<Resource>> downloadFile(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> {
            GridFsResource resource = fileService.getFile(id);
            MediaType contentType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
            try {
                CacheControl cacheControl = CacheControl.maxAge(Duration.ofDays(1))
                        .cachePublic()
                        .immutable();
                return ResponseEntity.ok()
                        .cacheControl(cacheControl)
                        .eTag("/%s/".formatted(id))
                        .contentType(contentType)
                        .contentLength(resource.contentLength())
                        .body(new InputStreamResource(resource.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
