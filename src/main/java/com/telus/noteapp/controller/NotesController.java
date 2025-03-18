package com.telus.noteapp.controller;

import com.telus.noteapp.modal.Note;
import com.telus.noteapp.service.NotesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for managing Notes.
 * Provides endpoints for CRUD operations and additional functionalities like counting notes and calculating word count.
 */
@RestController
@RequestMapping("/api/notes")
public class NotesController {

    // The service layer responsible for business logic
    private final NotesService notesService;

    // Logger for this class
    private static final Logger logger = LoggerFactory.getLogger(NotesController.class);

    /**
     * Constructor that initializes the controller with a NotesService.
     *
     * @param notesService The NotesService instance to be injected.
     */
    @Autowired
    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    /**
     * Endpoint to add a new note.
     *
     * @param note The note to be added.
     * @return ResponseEntity containing the added note and the HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Note> addNote(@RequestBody Note note) {
        logger.info("Request to add a new note: {}", note);
        Note addedNote = notesService.addNote(note);
        logger.info("Note added successfully: {}", addedNote);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedNote);
    }

    /**
     * Endpoint to modify an existing note.
     *
     * @param id          The ID of the note to be modified.
     * @param noteDetails The new details of the note.
     * @return ResponseEntity containing the updated note.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Note> modifyNote(@PathVariable Long id, @RequestBody Note noteDetails) {
        logger.info("Request to modify note with ID {}: {}", id, noteDetails);
        Note modifiedNote = notesService.modifyNote(id, noteDetails);
        logger.info("Note with ID {} modified successfully: {}", id, modifiedNote);
        return ResponseEntity.ok(modifiedNote);
    }

    /**
     * Endpoint to delete a note.
     *
     * @param id The ID of the note to be deleted.
     * @return ResponseEntity with a map indicating whether the note was deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteNote(@PathVariable Long id) {
        logger.info("Request to delete note with ID {}", id);
        notesService.deleteNote(id);

        // Preparing response indicating successful deletion
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        logger.info("Note with ID {} deleted successfully", id);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to search for notes by subject.
     *
     * @param subject The subject to search for.
     * @return ResponseEntity containing a list of notes matching the subject.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Note>> searchNotesBySubject(@RequestParam String subject) {
        logger.info("Request to search notes by subject: {}", subject);
        List<Note> notes = notesService.searchNotesBySubject(subject);
        logger.info("Found {} notes matching subject: {}", notes.size(), subject);
        return ResponseEntity.ok(notes);
    }

    /**
     * Endpoint to retrieve all notes.
     *
     * @return ResponseEntity containing the list of all notes.
     */
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        logger.info("Request to get all notes");
        List<Note> notes = notesService.getAllNotes();
        logger.info("Returning {} notes", notes.size());
        return ResponseEntity.ok(notes);
    }

    /**
     * Endpoint to get a specific note by its ID.
     *
     * @param id The ID of the note to retrieve.
     * @return ResponseEntity containing the requested note.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        logger.info("Request to get note with ID {}", id);
        Note note = notesService.getNoteById(id);
        logger.info("Returning note with ID {}: {}", id, note);
        return ResponseEntity.ok(note);
    }

    /**
     * Endpoint to get the total count of notes.
     *
     * @return ResponseEntity containing the total number of notes.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTotalNotes() {
        logger.info("Request to get total count of notes");
        long count = notesService.countTotalNotes();
        logger.info("Total number of notes: {}", count);
        return ResponseEntity.ok(count);
    }

    /**
     * Endpoint to get the word count of a note by its ID.
     *
     * @param id The ID of the note whose word count is to be calculated.
     * @return ResponseEntity containing the word count of the note.
     */
    @GetMapping("/word-count/{id}")
    public ResponseEntity<Integer> getWordCount(@PathVariable Long id) {
        logger.info("Request to get word count for note with ID {}", id);
        int wordCount = notesService.getWordCount(id);
        logger.info("Word count for note with ID {}: {}", id, wordCount);
        return ResponseEntity.ok(wordCount);
    }

    /**
     * Endpoint to get the average note length (in terms of word count).
     *
     * @return ResponseEntity containing the average note length in words.
     */
    @GetMapping("/average-length")
    public ResponseEntity<Double> getAverageNoteLength() {
        logger.info("Request to get average note length");
        double avgLength = notesService.getAverageNoteLength();
        logger.info("Average note length: {}", avgLength);
        return ResponseEntity.ok(avgLength);
    }

    /**
     * Endpoint to like a note.
     *
     * @param id The ID of the note to be liked.
     * @return ResponseEntity containing the updated note with increased likes.
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<Note> likeNote(@PathVariable Long id) {
        logger.info("Request to like note with ID {}", id);
        Note updatedNote = notesService.likeNote(id);
        logger.info("Note with ID {} liked successfully: {}", id, updatedNote);
        return ResponseEntity.ok(updatedNote);
    }

    /**
     * Endpoint to unlike a note.
     *
     * @param id The ID of the note to be unliked.
     * @return ResponseEntity containing the updated note with decreased likes.
     */
    @DeleteMapping("/{id}/unlike")
    public ResponseEntity<Note> unlikeNote(@PathVariable Long id) {
        logger.info("Request to unlike note with ID {}", id);
        Note updatedNote = notesService.unlikeNote(id);
        logger.info("Note with ID {} unliked successfully: {}", id, updatedNote);
        return ResponseEntity.ok(updatedNote);
    }

    /**
     * Endpoint to retrieve all liked notes.
     *
     * @return ResponseEntity containing the list of notes with likes greater than 0.
     */
    @GetMapping("/liked")
    public ResponseEntity<List<Note>> getLikedNotes() {
        logger.info("Request to get all liked notes");
        List<Note> likedNotes = notesService.getLikedNotes();
        logger.info("Found {} liked notes", likedNotes.size());
        return ResponseEntity.ok(likedNotes);
    }

    /**
     * Retrieves the top 5 most liked notes.
     *
     * @return a ResponseEntity containing the list of top 5 liked notes
     */
    @GetMapping("/top-liked")
    public ResponseEntity<List<Note>> getTopLikedNotes() {
        logger.info("Request to get top 5 liked notes");
        List<Note> topLikedNotes = notesService.getTopLikedNotes();
        logger.info("Returning top 5 liked notes");
        return ResponseEntity.ok(topLikedNotes);
    }

    /**
     * Boosts the like count of a note by 10.
     *
     * @param id the ID of the note to boost likes
     * @return a ResponseEntity containing a success message and the updated like count
     */
    @PostMapping("/{id}/like-boost")
    public ResponseEntity<Map<String, Object>> boostLikes(@PathVariable Long id) {
        logger.info("Request to boost likes for note with ID {}", id);
        Note updatedNote = notesService.boostLikes(id);
        logger.info("Note with ID {} boosted successfully. New like count: {}", id, updatedNote.getLikes());
        return ResponseEntity.ok(Map.of(
                "message", "Like Boost Activated!",
                "TotalLikes", updatedNote.getLikes()
        ));
    }

    /**
     * Resets the like count of a note to 0 (admin only).
     *
     * @param id the ID of the note to reset likes
     * @return a ResponseEntity containing a success message and the updated like count
     */
    @DeleteMapping("/{id}/like-reset")
    public ResponseEntity<Map<String, Object>> resetLikes(@PathVariable Long id) {
        logger.info("Request to reset likes for note with ID {}", id);
        Note updatedNote = notesService.resetLikes(id);
        logger.info("Likes for note with ID {} reset successfully. New like count: {}", id, updatedNote.getLikes());
        return ResponseEntity.ok(Map.of(
                "message", "All like resets",
                "TotalLikes", updatedNote.getLikes()
        ));
    }
}
