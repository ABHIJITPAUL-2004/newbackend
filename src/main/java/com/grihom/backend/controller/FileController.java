package com.grihom.backend.controller;

import com.grihom.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileStorageService fileStorageService;

    // POST /api/files/upload → ADMIN, DECOR
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN','DECOR')")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "No file provided."));
        }

        String filename = fileStorageService.storeFile(file);
        String fileUrl = "/api/files/" + filename;
        log.info("File uploaded: {} → {}", file.getOriginalFilename(), filename);

        return ResponseEntity.ok(Map.of(
                "filename", filename,
                "url", fileUrl,
                "originalName", file.getOriginalFilename() != null ? file.getOriginalFilename() : filename,
                "size", String.valueOf(file.getSize())
        ));
    }

    // GET /api/files/{filename} → Public (serve uploaded images)
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource resource = fileStorageService.loadFile(filename);

        String contentType;
        try {
            contentType = Files.probeContentType(resource.getFile().toPath());
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(
                        contentType != null ? contentType : "application/octet-stream"))
                .body(resource);
    }
}
