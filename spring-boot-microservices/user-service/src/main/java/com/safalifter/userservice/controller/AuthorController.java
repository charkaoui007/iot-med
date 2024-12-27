package com.safalifter.userservice.controller;// authorservice/src/main/java/com/safalifter/authorservice/controller/AuthorController.java


import com.safalifter.userservice.dto.AuthorDto;
import com.safalifter.userservice.request.CreateAuthorRequest;
import com.safalifter.userservice.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody CreateAuthorRequest request) {
        return ResponseEntity.ok(authorService.createAuthor(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable String id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EDITOR') or hasRole('ADMIN')")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable String id, @RequestBody CreateAuthorRequest request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAuthor(@PathVariable String id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok().build();
    }
}