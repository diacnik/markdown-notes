package app.markdownotes.data;

import java.util.UUID;

public record NoteInsert(
        long folderId,
        UUID accountId,
        String fileName,
        String storageFileName,
        String url
) {}
