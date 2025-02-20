package com.telus.demo.dao;

import com.telus.demo.modal.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Note entities.
 * Extends JpaRepository to provide basic CRUD operations for Note objects.
 */
public interface NotesRepository extends JpaRepository<Note, Long> {

    /**
     * Finds all notes whose subject contains the given string, case-insensitive.
     *
     * @param subject The subject string to search for.
     * @return A list of notes that contain the given subject string.
     */
    List<Note> findBySubjectContainingIgnoreCase(String subject);

    /**
     * Finds all notes that have more than a specified number of likes.
     *
     * @param likes The minimum number of likes.
     * @return A list of notes with likes greater than the specified value.
     */
    List<Note> findByLikesGreaterThan(int likes);
}
