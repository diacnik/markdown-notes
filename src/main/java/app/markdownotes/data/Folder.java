package app.markdownotes.data;

import java.util.UUID;

public record Folder(
        long id,
        UUID accountId,
        String name,
        String path
) {
}
