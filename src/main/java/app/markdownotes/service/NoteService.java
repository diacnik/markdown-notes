package app.markdownotes.service;

import app.markdownotes.data.Note;
import app.markdownotes.data.NoteInsert;
import app.markdownotes.data.NoteRequest;
import app.markdownotes.repository.NoteRepository;
import app.markdownotes.storage.CloudStorageAdapter;
import app.markdownotes.utility.text.MarkdownFormatter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Service class for Notes. Calls static converter and stores formatted notes via repository.
 */
@ApplicationScoped
public class NoteService {

    @Inject
    CloudStorageAdapter storage;

    @Inject
    NoteRepository repository;

    public void delete(Note note) {
        storage.deleteMarkdown(note.storageFileName());
        repository.delete(note);
    }

    public String getMarkdown(Note note) {
        return storage.getMarkdown(note.storageFileName());
    }

    public void formatAndSaveNote(File uploadedFile, NoteRequest noteReq) throws IOException {
        String markdownText;
        String storageUrl;
        String uploadedFileName = uploadedFile.getName();
        String uniqueFileName = generateUniqueFileName(uploadedFile);

        try(Stream<String> lines = Files.lines(uploadedFile.toPath())) {
            markdownText = MarkdownFormatter.formatToMD(lines);
        }

        storageUrl = storage.uploadMarkdown(uniqueFileName, markdownText);

        NoteInsert insertNote = new NoteInsert(noteReq.folderId(), noteReq.accountId(), uploadedFileName, uniqueFileName, storageUrl);

        try {
            repository.insert(insertNote);
        } catch (Exception dbEx) {
            storage.deleteMarkdown(uniqueFileName);
            throw new RuntimeException("Failed to insert note into database", dbEx);
        }
    }

    public void overwriteNote(File uploadedFile, Note note) throws IOException {
        String markdownText;
        String storageUrl;

        try(Stream<String> lines = Files.lines(uploadedFile.toPath())) {
            markdownText = MarkdownFormatter.formatToMD(lines);
        }

        storageUrl = storage.uploadMarkdown(note.storageFileName(), markdownText);

        try {
            repository.update(note);
        } catch (Exception dbEx) {
            storage.deleteMarkdown(note.storageFileName());
            throw new RuntimeException("Failed to update note in database", dbEx);
        }
    }

    private String generateUniqueFileName(File uploadedFile) {
        return uploadedFile.getName() + "-" + UUID.randomUUID().toString();
    }
}
