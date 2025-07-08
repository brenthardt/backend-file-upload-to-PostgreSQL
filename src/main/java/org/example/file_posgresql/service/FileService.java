package org.example.file_posgresql.service;

import org.example.file_posgresql.entity.FileEntity;
import org.example.file_posgresql.repository.FileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@Transactional
public class FileService {
    private final FileRepository fileRepository;
    private static final String UPLOAD_DIR = "files/";

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    public FileEntity saveFile(MultipartFile file) throws IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(UPLOAD_DIR, fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(file.getOriginalFilename());
        fileEntity.setType(file.getContentType());
        fileEntity.setPath(filePath.toString().replace("\\", "/"));

        return fileRepository.save(fileEntity);
    }

    public boolean deleteFile(Integer id){
        return fileRepository.findById(id).map(file->{
            Path filePath = Paths.get(file.getPath());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                return false;
            }
            fileRepository.delete(file);
            return true;
        }).orElse(false);
    }

    public boolean existsByName(String name) {
        return fileRepository.existsByName(name);
    }


}
