package com.giadinh.banphutung.web_ban_hang_gia_dinh.service;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.NoteDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.Note;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.entity.User;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.exception.ResourceNotFoundException;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.mapper.NoteMapper;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.NoteRepository;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteMapper noteMapper;

    public Page<NoteDto> list(Long userId, String keyword, Boolean completed, Pageable pageable) {
        if (keyword != null && !keyword.isBlank()) {
            return noteRepository.search(userId, keyword, completed, pageable).map(noteMapper::toDto);
        }
        return noteRepository.findByUserIdAndIsActiveTrue(userId, pageable).map(noteMapper::toDto);
    }

    public NoteDto get(Long id) {
        Note note = noteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note không tồn tại"));
        return noteMapper.toDto(note);
    }

    public NoteDto create(NoteDto dto) {
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("Thiếu userId");
        }
        User user = userRepository.findByIdAndIsActiveTrue(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        Note note = new Note();
        note.setUser(user);
        note.setIsActive(true);
        noteMapper.updateEntityFromDto(dto, note);
        Note saved = noteRepository.save(note);
        return noteMapper.toDto(saved);
    }

    public NoteDto update(Long id, NoteDto dto) {
        Note note = noteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note không tồn tại"));
        if (dto.getUserId() != null && (note.getUser() == null || !note.getUser().getId().equals(dto.getUserId()))) {
            User user = userRepository.findByIdAndIsActiveTrue(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
            note.setUser(user);
        }
        noteMapper.updateEntityFromDto(dto, note);
        Note saved = noteRepository.save(note);
        return noteMapper.toDto(saved);
    }

    public void delete(Long id) {
        Note note = noteRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note không tồn tại"));
        note.setIsActive(false);
        noteRepository.save(note);
    }
}

