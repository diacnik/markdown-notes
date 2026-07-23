package app.markdownotes.service;

import app.markdownotes.data.Folder;
import app.markdownotes.repository.FolderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class FolderService {

    @Inject
    private FolderRepository folderRepository;

    public List<Folder> getAll() {
        return folderRepository.getAllFolders();
    }

    public Optional<Folder> getFolder(long id) {
        return folderRepository.getFolderById(id);
    }

    public List<Folder> getFoldersByAccountId(UUID accountId) {
        return folderRepository.getFoldersByAccountId(accountId);
    }


    // methods for changing folder structure
}
