package com.giadinh.banphutung.web_ban_hang_gia_dinh.controller;

import com.giadinh.banphutung.web_ban_hang_gia_dinh.dto.NoteDto;
import com.giadinh.banphutung.web_ban_hang_gia_dinh.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<Page<NoteDto>> list(@RequestParam Long userId,
                                              @RequestParam(required = false) String keyword,
                                              @RequestParam(required = false) Boolean completed,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(noteService.list(userId, keyword, completed, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.get(id));
    }

    @PostMapping
    public ResponseEntity<NoteDto> create(@RequestBody NoteDto dto) {
        return ResponseEntity.status(201).body(noteService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> update(@PathVariable Long id, @RequestBody NoteDto dto) {
        return ResponseEntity.ok(noteService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

