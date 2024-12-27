package com.safalifter.userservice.service;

import com.safalifter.userservice.dto.AuthorDto;
import com.safalifter.userservice.model.Author;
import com.safalifter.userservice.repository.AuthorRepository;
import com.safalifter.userservice.request.CreateAuthorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorDto createAuthor(CreateAuthorRequest request) {
        Author author = Author.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .build();
        Author savedAuthor = authorRepository.save(author);
        return mapToDto(savedAuthor);
    }

    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public AuthorDto getAuthorById(String id) {
        return authorRepository.findById(id)
                .map(this::mapToDto)
                .orElse(null); // Consider throwing a NotFoundException
    }

    public AuthorDto updateAuthor(String id, CreateAuthorRequest request) {
        return authorRepository.findById(id)
                .map(existingAuthor -> {
                    existingAuthor.setName(request.getName());
                    Author updatedAuthor = authorRepository.save(existingAuthor);
                    return mapToDto(updatedAuthor);
                })
                .orElse(null); // Consider throwing a NotFoundException
    }

    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }

    private AuthorDto mapToDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .build();
    }
}