package com.telus.demo.controller;

import com.telus.demo.modal.Note;
import com.telus.demo.service.NotesService;
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
        // Adds a note via the NotesService and responds with the created note.
        return ResponseEntity.status(HttpStatus.CREATED).body(notesService.addNote(note));
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
        // Modifies the note with the given ID and responds with the updated note.
        return ResponseEntity.ok(notesService.modifyNote(id, noteDetails));
    }

    /**
     * Endpoint to delete a note.
     *
     * @param id The ID of the note to be deleted.
     * @return ResponseEntity with a map indicating whether the note was deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteNote(@PathVariable Long id) {
        // Deletes the note with the given ID.
        notesService.deleteNote(id);

        // Preparing response indicating successful deletion
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
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
        // Searches for notes with a subject containing the given string and returns the list of matching notes.
        return ResponseEntity.ok(notesService.searchNotesBySubject(subject));
    }

    /**
     * Endpoint to retrieve all notes.
     *
     * @return ResponseEntity containing the list of all notes.
     */
    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        // Retrieves all notes and returns them in the response.
        return ResponseEntity.ok(notesService.getAllNotes());
    }

    /**
     * Endpoint to get a specific note by its ID.
     *
     * @param id The ID of the note to retrieve.
     * @return ResponseEntity containing the requested note.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        // Retrieves a note by its ID and returns it in the response.
        return ResponseEntity.ok(notesService.getNoteById(id));
    }

    /**
     * Endpoint to get the total count of notes.
     *
     * @return ResponseEntity containing the total number of notes.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTotalNotes() {
        // Retrieves and returns the total number of notes in the system.
        return ResponseEntity.ok(notesService.countTotalNotes());
    }

    /**
     * Endpoint to get the word count of a note by its ID.
     *
     * @param id The ID of the note whose word count is to be calculated.
     * @return ResponseEntity containing the word count of the note.
     */
    @GetMapping("/word-count/{id}")
    public ResponseEntity<Integer> getWordCount(@PathVariable Long id) {
        // Retrieves the word count of the note with the given ID.
        return ResponseEntity.ok(notesService.getWordCount(id));
    }

    /**
     * Endpoint to get the average note length (in terms of word count).
     *
     * @return ResponseEntity containing the average note length in words.
     */
    @GetMapping("/average-length")
    public ResponseEntity<Double> getAverageNoteLength() {
        // Calculates and returns the average length of all notes.
        return ResponseEntity.ok(notesService.getAverageNoteLength());
    }

    /**
     * Endpoint to like a note.
     *
     * @param id The ID of the note to be liked.
     * @return ResponseEntity containing the updated note with increased likes.
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<Note> likeNote(@PathVariable Long id) {
        // Increments the like count of the note with the given ID.
        return ResponseEntity.ok(notesService.likeNote(id));
    }

    /**
     * Endpoint to unlike a note.
     *
     * @param id The ID of the note to be unliked.
     * @return ResponseEntity containing the updated note with decreased likes.
     */
    @DeleteMapping("/{id}/unlike")
    public ResponseEntity<Note> unlikeNote(@PathVariable Long id) {
        // Decrements the like count of the note with the given ID.
        return ResponseEntity.ok(notesService.unlikeNote(id));
    }

    /**
     * Endpoint to retrieve all liked notes.
     *
     * @return ResponseEntity containing the list of notes with likes greater than 0.
     */
    @GetMapping("/liked")
    public ResponseEntity<List<Note>> getLikedNotes() {
        // Retrieves all notes that have at least one like.
        return ResponseEntity.ok(notesService.getLikedNotes());
    }

    /**
     * Retrieves the top 5 most liked notes.
     *
     * @return a ResponseEntity containing the list of top 5 liked notes
     */
    @GetMapping("/top-liked")
    public ResponseEntity<List<Note>> getTopLikedNotes() {
        return ResponseEntity.ok(notesService.getTopLikedNotes());
    }

    /**
     * ̥
     * Boosts the like count of a note by 10.
     *
     * @param id the ID of the note to boost likes
     * @return a ResponseEntity containing a success message and the updated like count
     */
    @PostMapping("/{id}/like-boost")
    public ResponseEntity<Map<String, Object>> boostLikes(@PathVariable Long id) {
        Note updatedNote = notesService.boostLikes(id);
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
        Note updatedNote = notesService.resetLikes(id);
        return ResponseEntity.ok(Map.of(
                "message", "All like reset!",
                "TotalLikes", updatedNote.getLikes()
        ));
    }


}


