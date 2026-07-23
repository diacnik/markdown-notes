package app.markdownotes.resource;

import app.markdownotes.service.FolderService;
import com.google.api.services.storage.model.Folder;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

@Path("/folder")
public class FolderResource {

    @Inject
    private FolderService folderService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Folder> getAllFolders() {
        //TODO: implement
        return null;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Folder getFolder(@PathParam("id") long folderId) {
        //TODO: implement
        return null;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Folder> getFoldersByAccount(@PathParam("id") UUID rootId) {
        //TODO: implement
        return null;
    }
}
