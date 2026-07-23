package app.markdownotes.data;

import java.util.UUID;

public record FolderInsert(
        UUID account_id,
        String name,
        String path
) {
}
