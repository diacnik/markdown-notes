package app.markdownotes.data;

import java.util.List;

public record FolderNode(
        long id,
        String name,
        String path,
        List<FolderNode> children
) {
}
