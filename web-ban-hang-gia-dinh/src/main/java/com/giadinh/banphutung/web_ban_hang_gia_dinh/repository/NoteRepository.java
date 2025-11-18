package com.giadinh.banphutung.web_ban_hang_gia_dinh.repository;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Optional<Note> findByIdAndIsActiveTrue(Long id);

    Page<Note> findByUserIdAndIsActiveTrue(Long userId, Pageable pageable);
    @Query("SELECT n FROM Note n WHERE n.isActive = true AND n.user.id = :userId " +
            "AND (:completed IS NULL OR n.completed = :completed) " +
            "AND (:keyword IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Note> search(@Param("userId") Long userId,
                      @Param("keyword") String keyword,
                      @Param("completed") Boolean completed,
                      Pageable pageable);
}
