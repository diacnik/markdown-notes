package app.markdownotes.repository;

import app.markdownotes.data.Note;
import app.markdownotes.data.NoteInsert;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class NoteRepository {

    @Inject
    JdbcPipeline jdbcPipeline;

    public void delete(Note note) {
        String sql = "DELETE FROM note WHERE id = ?";
        jdbcPipeline.executeUpdate(sql, prepStmt -> prepStmt.setLong(1, note.id()));
    }

    public List<Note> getAll() {
        String sql = "SELECT * FROM note";
        return jdbcPipeline.executeQuery(
                sql,
                prepStmt -> {},
                resultSet ->  {
                    List<Note> notes = new ArrayList<>();
                    while (resultSet.next()) {
                        notes.add(mapRowToNote(resultSet));
                    }
                    return notes;
                }
        );
    }

    public Optional<Note> getNoteById(long id) {
        String sql = """
                SELECT * FROM note WHERE id = ?;
                """;

        return jdbcPipeline.executeQuery(
                sql,
                prepStmt -> prepStmt.setLong(1, id),
                resultSet -> resultSet.next() ? Optional.of(mapRowToNote(resultSet)) : Optional.empty()
        );
    }

    public List<Note> getNoteByAccountId(UUID accountId) {
        String sql = """
                SELECT * FROM note WHERE account_id = ?;
        """;

        return jdbcPipeline.executeQuery(
                sql,
                prepStmt -> prepStmt.setObject(1, accountId),
                resultSet ->   {
                    List<Note> notes = new ArrayList<>();
                    while (resultSet.next()) {
                        notes.add(mapRowToNote(resultSet));
                    }
                    return notes;
                }
        );
    }

    public List<Note> getNotesByFolderId(long folderId) {
        String sql = """
                SELECT * FROM note WHERE folder_id = ?;
        """;

        return jdbcPipeline.executeQuery(
                sql,
                prepStmt -> prepStmt.setLong(1, folderId),
                resultSet ->   {
                    List<Note> notes = new ArrayList<>();
                    while (resultSet.next()) {
                        notes.add(mapRowToNote(resultSet));
                    }
                    return notes;
                }
        );
    }



    public void insert(NoteInsert note) {
        String sql = """
                INSERT INTO note (
                    folder_id,
                    account_id,
                    file_name,
                    storage_file_name,
                    storage_url
                )
                VALUES (?, ?, ?, ?, ?);
        """;

        jdbcPipeline.executeUpdate(sql, prepStmt -> bindNoteInsertToPreparedStatement(prepStmt, note));
    }

    public void update(Note note) {
        String sql = """
                UPDATE note
                SET
                    folder_id = ?,
                    account_id = ?,
                    file_name = ?,
                    storage_file_name = ?,
                    storage_url = ?
                WHERE id = ?;
        """;

        jdbcPipeline.executeUpdate(
                sql,
                prepStmt -> {
                    bindNoteToPreparedStatement(prepStmt, note);
                    prepStmt.setLong(6, note.id());
                }
        );
    }

    private static PreparedStatement bindNoteToPreparedStatement(PreparedStatement prepStmt, Note note) throws SQLException {
        prepStmt.setLong(1, note.folderId());
        prepStmt.setObject(2, note.accountId());
        prepStmt.setString(3, note.fileName());
        prepStmt.setString(4, note.storageFileName());
        prepStmt.setString(5, note.url());
        return prepStmt;
    }

    private static PreparedStatement bindNoteInsertToPreparedStatement(PreparedStatement prepStmt, NoteInsert note) throws SQLException {
        prepStmt.setLong(1, note.folderId());
        prepStmt.setObject(2, note.accountId());
        prepStmt.setString(3, note.fileName());
        prepStmt.setString(4, note.storageFileName());
        prepStmt.setString(5, note.url());
        return prepStmt;
    }

    private static Note mapRowToNote(ResultSet resultSet) throws SQLException {
        return new Note(
                resultSet.getLong("id"),
                resultSet.getLong("folder_id"),
                resultSet.getObject("account_id", java.util.UUID.class),
                resultSet.getString("file_name"),
                resultSet.getString("storage_file_name"),
                resultSet.getString("storage_url")
        );
    }
}
