package app.markdownotes.service;

import app.markdownotes.repository.FolderRepository;
import com.google.api.services.storage.model.Folder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;

import java.util.List;

@ApplicationScoped
public class FolderService {

    @Inject
    private FolderRepository folderRepository;

    // methods for changing folder structure
}
