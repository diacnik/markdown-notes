package app.markdownotes.data;

import java.util.UUID;

public record Note(
        long id,
        long folderId,
        UUID accountId,
        String url
) {}
