package app.markdownotes.data;

import java.util.List;

public record FolderTree(
        long id,
        String name,
        String path,
        List<FolderTree> children
) {
}
