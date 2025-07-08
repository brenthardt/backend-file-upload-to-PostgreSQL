package org.example.file_posgresql.repository;
import org.example.file_posgresql.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    boolean existsByName(String name);
}

