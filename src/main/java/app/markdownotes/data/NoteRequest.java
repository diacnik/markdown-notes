package app.markdownotes.data;

import java.util.UUID;

public record NoteRequest(
        long folderId,
        UUID accountId
) {}
