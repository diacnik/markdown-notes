package app.markdownotes.resource;

import app.markdownotes.data.Note;
import app.markdownotes.data.NoteRequest;
import app.markdownotes.data.NoteMarkdown;
import app.markdownotes.service.NoteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Path("/notes")
public class NoteResource {

    @Inject
    private NoteService noteService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Note> getNotes() {
        return noteService.getAllNotes();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public NoteMarkdown getNote(@PathParam("id") long id) {
        return noteService.getMarkdownNoteById(id);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Note> getNotesInFolder(@PathParam("id") long folderId) {
        return noteService.getNotesByFolderId(folderId);
    }

    @POST
    public Response createNote(File uploadedFile, NoteRequest noteRequest) throws IOException {
        noteService.formatAndSaveNote(uploadedFile, noteRequest);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    public Response updateNote(File uploadedFile, Note note) throws IOException {
        noteService.overwriteNote(uploadedFile, note);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteNote(Note note) {
        noteService.delete(note);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
