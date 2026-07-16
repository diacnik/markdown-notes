package app.markdownotes.service;

import app.markdownotes.utility.text.MarkdownFormatter;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

/**
 * Service class for Notes. Calls static converter and stores formatted notes via repository.
 */
@ApplicationScoped
public class NoteService {

    /**
     * Converts .txt file to .md and formats text. Saves markdown notes via repository.
     * @param uploadedFile txt file to be converted to md
     * @return text formatted for md
     * @throws IOException
     */
    public String convertAndSaveNote(File uploadedFile) throws IOException {
        String markdownText;

        try(Stream<String> lines = Files.lines(uploadedFile.toPath())) {
            markdownText = MarkdownFormatter.formatToMD(lines);
        }

        // TODO: Save file to google cloud storage and url to database
        return markdownText; // return formatted text for now
    }
}
