package com.medici.university.repository;

import com.medici.university.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

	Optional<File> findByIdAndDeletedFalse(Long id);

	Page<File> findAllByGroupIdAndDeletedFalseOrderByCreatedOnDesc(Long groupId, Pageable pageable);


}
