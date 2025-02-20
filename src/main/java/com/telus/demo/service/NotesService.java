package com.telus.demo.service;

import com.telus.demo.dao.NotesRepository;
import com.telus.demo.exception.NoteNotFoundException;
import com.telus.demo.modal.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing notes. This class handles the business logic related to
 * notes, including CRUD operations and additional functionalities like counting notes,
 * calculating word counts, and managing likes.
 */
@Service
@Slf4j
public class NotesService {
    private final NotesRepository noteRepository;

    /**
     * Constructor for NotesService.
     *
     * @param noteRepository The repository used to interact with the data source.
     */
    public NotesService(NotesRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Adds a new note to the system.
     *
     * @param note The note to be added.
     * @return The saved note.
     */
    public Note addNote(Note note) {
        log.info("Adding a new note with subject: {}", note.getSubject());
        note.setTimestampCreated(LocalDateTime.now());  // Setting creation timestamp
        note.setTimestampUpdated(LocalDateTime.now());  // Setting update timestamp
        Note savedNote = noteRepository.save(note);
        log.info("Note with ID {} added successfully", savedNote.getNoteId());
        return savedNote;
    }

    /**
     * Modifies an existing note by its ID.
     *
     * @param id          The ID of the note to modify.
     * @param noteDetails The updated note details.
     * @return The updated note.
     * @throws ResponseStatusException If the note with the given ID is not found.
     */
    public Note modifyNote(Long id, Note noteDetails) {
        log.info("Modifying note with ID {}", id);
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with ID " + id + " not found"));

        note.setSubject(noteDetails.getSubject());
        note.setDescription(noteDetails.getDescription());
        note.setTimestampUpdated(LocalDateTime.now());  // Updating timestamp
        Note updatedNote = noteRepository.save(note);
        log.info("Note with ID {} modified successfully", updatedNote.getNoteId());
        return updatedNote;
    }

    /**
     * Deletes a note by its ID.
     *
     * @param id The ID of the note to delete.
     * @throws ResponseStatusException If the note with the given ID is not found.
     */
    public void deleteNote(Long id) {
        log.info("Deleting note with ID {}", id);
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with ID " + id + " not found"));
        noteRepository.delete(note);
        log.info("Note with ID {} deleted successfully", id);
    }

    /**
     * Searches for notes by a subject string (case-insensitive).
     *
     * @param subject The subject string to search for.
     * @return A list of notes that contain the given subject.
     */
    public List<Note> searchNotesBySubject(String subject) {
        log.info("Searching notes with subject containing: {}", subject);
        List<Note> notes = noteRepository.findBySubjectContainingIgnoreCase(subject);
        log.info("Found {} notes with subject containing: {}", notes.size(), subject);
        return notes;
    }

    /**
     * Fetches a note by its ID.
     *
     * @param id The ID of the note to retrieve.
     * @return The note corresponding to the given ID.
     * @throws ResponseStatusException If the note with the given ID is not found.
     */
    public Note getNoteById(Long id) {
        log.info("Fetching note with ID {}", id);
        return noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with ID " + id + " not found"));
    }

    /**
     * Counts the total number of notes in the system.
     *
     * @return The total number of notes.
     */
    public Long countTotalNotes() {
        log.info("Counting total number of notes");
        long count = noteRepository.count();
        log.info("Total number of notes: {}", count);
        return count;
    }

    /**
     * Calculates the word count for a given note by its ID.
     *
     * @param id The ID of the note.
     * @return The word count of the note's description.
     * @throws ResponseStatusException If the note with the given ID is not found.
     */
    public Integer getWordCount(Long id) {
        log.info("Getting word count for note with ID {}", id);
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with ID " + id + " not found"));
        int wordCount = note.getDescription().split("\\s+").length;  // Count words by spaces
        log.info("Note with ID {} has {} words", id, wordCount);
        return wordCount;
    }

    /**
     * Calculates the average length of all notes' descriptions in terms of word count.
     *
     * @return The average word count across all notes.
     */
    public Double getAverageNoteLength() {
        log.info("Calculating the average note length");
        List<Note> notes = noteRepository.findAll();
        double averageLength = notes.stream()
                .mapToInt(note -> note.getDescription().split("\\s+").length)  // Map to word counts
                .average()
                .orElse(0.0);  // Default to 0.0 if no notes exist
        log.info("Average note length: {} words", averageLength);
        return averageLength;
    }

    /**
     * Likes a note, increasing its like count by 1.
     *
     * @param id The ID of the note to like.
     * @return The updated note with the new like count.
     * @throws ResponseStatusException If the note with the given ID is not found.
     */
    public Note likeNote(Long id) {
        log.info("Liking note with ID {}", id);
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with ID " + id + " not found"));
        note.setLikes(note.getLikes() + 1);  // Increment the like count
        note.setTimestampUpdated(LocalDateTime.now());  // Update timestamp
        Note updatedNote = noteRepository.save(note);
        log.info("Note with ID {} liked successfully. Total likes: {}", id, updatedNote.getLikes());
        return updatedNote;
    }

    /**
     * Unlikes a note, decreasing its like count by 1 (minimum 0).
     *
     * @param id The ID of the note to unlike.
     * @return The updated note with the new like count.
     * @throws ResponseStatusException If the note with the given ID is not found.
     */
    public Note unlikeNote(Long id) {
        log.info("Unliking note with ID {}", id);
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with ID " + id + " not found"));
        note.setLikes(Math.max(note.getLikes() - 1, 0));  // Decrease the like count, but not below 0
        note.setTimestampUpdated(LocalDateTime.now());  // Update timestamp
        Note updatedNote = noteRepository.save(note);
        log.info("Note with ID {} unliked successfully. Total likes: {}", id, updatedNote.getLikes());
        return updatedNote;
    }

    /**
     * Retrieves all notes that have been liked (likes > 0).
     *
     * @return A list of liked notes.
     */
    public List<Note> getLikedNotes() {
        log.info("Fetching all liked notes");
        List<Note> likedNotes = noteRepository.findByLikesGreaterThan(0);
        log.info("Found {} liked notes", likedNotes.size());
        return likedNotes;
    }

    /**
     * Retrieves all available notes.
     *
     * @return A list of all notes.
     */
    public List<Note> getAllNotes() {
        log.info("Fetching all available notes");
        return noteRepository.findAll();
    }

    /**
     * Retrieves the top 5 most liked notes.
     *
     * @return a list of top 5 most liked notes
     */
    public List<Note> getTopLikedNotes() {
        return noteRepository.findAll().stream()
                .sorted((n1, n2) -> Integer.compare(n2.getLikes(), n1.getLikes())) // Sort by likes in descending order
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Boosts the like count of a note by 10.
     *
     * @param id the ID of the note to be boosted
     * @return the updated note with boosted likes
     * @throws NoteNotFoundException if the note with the given ID is not found
     */
    public Note boostLikes(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with ID " + id + " not found"));
        note.setLikes(note.getLikes() + 10);
        return noteRepository.save(note);
    }

    /**
     * Resets the like count of a note to 0.
     *
     * @param id the ID of the note to reset likes
     * @return the updated note with 0 likes
     * @throws NoteNotFoundException if the note with the given ID is not found
     */
    public Note resetLikes(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with ID " + id + " not found"));
        note.setLikes(0);
        return noteRepository.save(note);
    }
}