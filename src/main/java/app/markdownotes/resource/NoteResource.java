package app.markdownotes.resource;

import app.markdownotes.data.Note;
import app.markdownotes.service.NoteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/notes")
public class NoteResource {

    @Inject
    private NoteService noteService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Note> getNotes() {
        return null;
    }
}
