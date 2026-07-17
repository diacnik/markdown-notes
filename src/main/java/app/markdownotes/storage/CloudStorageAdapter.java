package app.markdownotes.storage;

import app.markdownotes.data.NoteRequest;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class CloudStorageAdapter {

    @Inject
    Storage storage;

    @ConfigProperty(name = "note.storage.gcs.bucket-name")
    String bucketName;

    public String getMarkdown(String storageFileName) {
        BlobId blobId = BlobId.of(bucketName, storageFileName);

        Blob blob = storage.get(blobId);

        if (blob == null) {
            throw new RuntimeException("Could not find blob for file " + storageFileName);
        }

        byte[] content = blob.getContent();

        return new String(content, StandardCharsets.UTF_8);
    }

    public String uploadMarkdown(String fileName, String markdownText) {
        BlobId blobId = BlobId.of(bucketName, fileName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("text/markdown")
                .build();

        storage.create(blobInfo, markdownText.getBytes(StandardCharsets.UTF_8));

        return "gs://" + bucketName + "/" + fileName;
    }

    public boolean deleteMarkdown(String fileName) {
        BlobId blobId = BlobId.of(bucketName, fileName);
        return storage.delete(blobId);
    }
}
