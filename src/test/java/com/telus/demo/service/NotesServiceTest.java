package com.telus.demo.service;

import com.telus.demo.dao.NotesRepository;
import com.telus.demo.exception.NoteNotFoundException;
import com.telus.demo.modal.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class NotesServiceTest {

    @Mock
    private NotesRepository notesRepository;

    private NotesService notesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notesService = new NotesService(notesRepository);
    }

    @Test
    void testAddNote() {
        Note note = new Note();
        note.setSubject("Test Subject");
        note.setDescription("Test Description");

        when(notesRepository.save(any(Note.class))).thenReturn(note);

        Note createdNote = notesService.addNote(note);

        assertNotNull(createdNote);
        assertEquals("Test Subject", createdNote.getSubject());
        verify(notesRepository, times(1)).save(any(Note.class));
    }

    @Test
    void testModifyNote() {
        Long noteId = 1L;
        Note existingNote = new Note();
        existingNote.setNoteId(noteId);
        existingNote.setSubject("Old Subject");
        existingNote.setDescription("Old Description");

        Note updatedNoteDetails = new Note();
        updatedNoteDetails.setSubject("Updated Subject");
        updatedNoteDetails.setDescription("Updated Description");

        when(notesRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(notesRepository.save(any(Note.class))).thenReturn(updatedNoteDetails);

        Note modifiedNote = notesService.modifyNote(noteId, updatedNoteDetails);

        assertNotNull(modifiedNote);
        assertEquals("Updated Subject", modifiedNote.getSubject());
        assertEquals("Updated Description", modifiedNote.getDescription());
        verify(notesRepository, times(1)).findById(noteId);
        verify(notesRepository, times(1)).save(any(Note.class));
    }

    @Test
    void testDeleteNote() {
        Long noteId = 1L;
        Note note = new Note();
        note.setNoteId(noteId);

        when(notesRepository.findById(noteId)).thenReturn(Optional.of(note));

        notesService.deleteNote(noteId);

        verify(notesRepository, times(1)).findById(noteId);
        verify(notesRepository, times(1)).delete(note);
    }

    @Test
    void testDeleteNote_NotFound() {
        Long noteId = 1L;

        when(notesRepository.findById(noteId)).thenReturn(Optional.empty());

//        assertThrows(ResponseStatusException.class, () -> notesService.deleteNote(noteId));
        assertThrows(NoteNotFoundException.class, () -> notesService.deleteNote(noteId));
    }

    @Test
    void testSearchNotesBySubject() {
        String subject = "Test Subject";
        Note note = new Note();
        note.setSubject(subject);
        when(notesRepository.findBySubjectContainingIgnoreCase(subject)).thenReturn(List.of(note));

        List<Note> foundNotes = notesService.searchNotesBySubject(subject);

        assertNotNull(foundNotes);
        assertEquals(1, foundNotes.size());
        assertEquals(subject, foundNotes.get(0).getSubject());
        verify(notesRepository, times(1)).findBySubjectContainingIgnoreCase(subject);
    }

    @Test
    void testGetNoteById() {
        Long noteId = 1L;
        Note note = new Note();
        note.setNoteId(noteId);

        when(notesRepository.findById(noteId)).thenReturn(Optional.of(note));

        Note foundNote = notesService.getNoteById(noteId);

        assertNotNull(foundNote);
        assertEquals(noteId, foundNote.getNoteId());
        verify(notesRepository, times(1)).findById(noteId);
    }

    @Test
    void testGetNoteById_NotFound() {
        Long noteId = 1L;

        when(notesRepository.findById(noteId)).thenReturn(Optional.empty());

//        assertThrows(ResponseStatusException.class, () -> notesService.getNoteById(noteId));
        assertThrows(NoteNotFoundException.class, () -> notesService.getNoteById(noteId));
    }

    @Test
    void testCountTotalNotes() {
        when(notesRepository.count()).thenReturn(5L);

        Long totalNotes = notesService.countTotalNotes();

        assertEquals(5L, totalNotes);
        verify(notesRepository, times(1)).count();
    }

    @Test
    void testGetWordCount() {
        Long noteId = 1L;
        Note note = new Note();
        note.setNoteId(noteId);
        note.setDescription("This is a test note.");

        when(notesRepository.findById(noteId)).thenReturn(Optional.of(note));

        Integer wordCount = notesService.getWordCount(noteId);

        assertEquals(5, wordCount);
        verify(notesRepository, times(1)).findById(noteId);
    }

    @Test
    void testGetAverageNoteLength() {
        Note note1 = new Note();
        note1.setDescription("This is a test note.");
        Note note2 = new Note();
        note2.setDescription("This is another test note.");

        when(notesRepository.findAll()).thenReturn(List.of(note1, note2));

        Double averageLength = notesService.getAverageNoteLength();

        assertEquals(5.0, averageLength);
        verify(notesRepository, times(1)).findAll();
    }

    @Test
    void testLikeNote() {
        Long noteId = 1L;
        Note note = new Note();
        note.setNoteId(noteId);
        note.setLikes(0);

        when(notesRepository.findById(noteId)).thenReturn(Optional.of(note));
        when(notesRepository.save(any(Note.class))).thenReturn(note);

        Note likedNote = notesService.likeNote(noteId);

        assertNotNull(likedNote);
        assertEquals(1, likedNote.getLikes());
        verify(notesRepository, times(1)).findById(noteId);
        verify(notesRepository, times(1)).save(any(Note.class));
    }

    @Test
    void testUnlikeNote() {
        Long noteId = 1L;
        Note note = new Note();
        note.setNoteId(noteId);
        note.setLikes(1);

        when(notesRepository.findById(noteId)).thenReturn(Optional.of(note));
        when(notesRepository.save(any(Note.class))).thenReturn(note);

        Note unlikedNote = notesService.unlikeNote(noteId);

        assertNotNull(unlikedNote);
        assertEquals(0, unlikedNote.getLikes());
        verify(notesRepository, times(1)).findById(noteId);
        verify(notesRepository, times(1)).save(any(Note.class));
    }

    @Test
    void testGetLikedNotes() {
        Note likedNote = new Note();
        likedNote.setLikes(5);

        when(notesRepository.findByLikesGreaterThan(0)).thenReturn(List.of(likedNote));

        List<Note> likedNotes = notesService.getLikedNotes();

        assertNotNull(likedNotes);
        assertEquals(1, likedNotes.size());
        assertEquals(5, likedNotes.get(0).getLikes());
        verify(notesRepository, times(1)).findByLikesGreaterThan(0);
    }

    @Test
    void testGetAllNotes() {
        Note note1 = new Note();
        Note note2 = new Note();

        when(notesRepository.findAll()).thenReturn(List.of(note1, note2));

        List<Note> allNotes = notesService.getAllNotes();

        assertNotNull(allNotes);
        assertEquals(2, allNotes.size());
        verify(notesRepository, times(1)).findAll();
    }

    @Test
    public void testGetTopLikedNotes() {
        // Arrange
        List<Note> notes = Arrays.asList(
                Note.builder().noteId(1L).subject("Note 1").description("Description 1").likes(10).build(),
                Note.builder().noteId(1L).subject("Note 2").description("Description 2").likes(5).build(),
                Note.builder().noteId(1L).subject("Note 3").description("Description 3").likes(3).build()
        );
        when(notesRepository.findAll()).thenReturn(notes);

        // Act
        List<Note> topLikedNotes = notesService.getTopLikedNotes();

        // Assert
        assertEquals(3, topLikedNotes.size());
        assertEquals(10, topLikedNotes.get(0).getLikes()); // Note with 10 likes should be first
        assertEquals(5, topLikedNotes.get(1).getLikes()); // Note with 5 likes should be second
        assertEquals(3, topLikedNotes.get(2).getLikes()); // Note with 3 likes should be third
    }

    @Test
    public void testBoostLikes() {
        // Arrange
        Long noteId = 2L;
        Note note2 = Note.builder().noteId(1L).subject("Note 2").description("Description 2").likes(5).build();
        when(notesRepository.findById(noteId)).thenReturn(Optional.of(note2));
        when(notesRepository.save(note2)).thenReturn(note2);

        // Act
        Note updatedNote = notesService.boostLikes(noteId);

        // Assert
        assertEquals(15, updatedNote.getLikes()); // Likes should increase by 10
        verify(notesRepository, times(1)).save(note2); // Verify that save was called once
    }

    @Test
    public void testBoostLikes_NoteNotFound() {
        // Arrange
        Long noteId = 4L; // Non-existing note
        when(notesRepository.findById(noteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoteNotFoundException.class, () -> notesService.boostLikes(noteId));
    }

    @Test
    public void testResetLikes() {
        // Arrange
        Long noteId = 1L;
        Note note1 = Note.builder().noteId(1L).subject("Note 1").description("Description 1").likes(10).build();
        when(notesRepository.findById(noteId)).thenReturn(Optional.of(note1));
        when(notesRepository.save(note1)).thenReturn(note1);

        // Act
        Note updatedNote = notesService.resetLikes(noteId);

        // Assert
        assertEquals(0, updatedNote.getLikes()); // Likes should be reset to 0
        verify(notesRepository, times(1)).save(note1); // Verify that save was called once
    }

    @Test
    public void testResetLikes_NoteNotFound() {
        // Arrange
        Long noteId = 5L; // Non-existing note
        when(notesRepository.findById(noteId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoteNotFoundException.class, () -> notesService.resetLikes(noteId));
    }
}
