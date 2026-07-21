package app.markdownotes.data;

import java.util.UUID;

public record NoteMarkdown(
        long id,
        long folderId,
        UUID accountId,
        String fileName,
        String storageFileName,
        String url,
        String markdownText
) {
}
