package app.markdownotes.data;

public record Note(
        long id,
        long folderId,
        String url
) {}
