package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
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
    public InputStreamResource downloadFile(@PathVariable String id) throws IOException {
        return fileService.getFile(id);
    }
}
