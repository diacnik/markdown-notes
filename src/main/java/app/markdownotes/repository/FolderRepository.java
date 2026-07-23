package app.markdownotes.repository;

import app.markdownotes.data.Folder;
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
public class FolderRepository {

    @Inject
    private JdbcPipeline jdbcPipeline;

    public List<Folder> getAllFolders() {
        String sql = """
                SELECT id, account_id, name, path::text AS path_str
                FROM folder_node
                ORDER BY path;
                """;

        return jdbcPipeline.executeQuery(
                sql,
                preparedStatement -> {},
                resultSet -> {
                    List<Folder> folders = new ArrayList<>();
                    while (resultSet.next()) {
                        folders.add(mapRowToFolder(resultSet));
                    }
                    return folders;
                }
        );
    }

    public Optional<Folder> getFolderById(long id) {
        String sql = """
                SELECT id, account_id, name, path::text AS path_str
                FROM folder_node
                WHERE id = ?;
                """;

        return jdbcPipeline.executeQuery(
                sql,
                prepStmt -> prepStmt.setLong(1, id),
                resultSet -> resultSet.next() ? Optional.of(mapRowToFolder(resultSet)) : Optional.empty()
        );
    }

    public List<Folder> getFoldersByAccountId(UUID accountId) {
        String sql = """
                SELECT id, account_id, name, path::text AS path_str
                FROM folder_node
                WHERE account_id = ?
                ORDER BY path;
                """;

        return jdbcPipeline.executeQuery(
                sql,
                preparedStatement -> preparedStatement.setObject(1, accountId),
                resultSet -> {
                    List<Folder> folders = new ArrayList<>();
                    while (resultSet.next()) {
                        folders.add(mapRowToFolder(resultSet));
                    }
                    return folders;
                }
        );
    }

    private PreparedStatement bindFolderToPreparedStatement(PreparedStatement prepStmt, Folder folder) throws SQLException {
        prepStmt.setLong(1, folder.id());
        prepStmt.setObject(2, folder.accountId());
        prepStmt.setString(3, folder.name());
        prepStmt.setString(4, folder.path());
        return prepStmt;
    }

    private PreparedStatement bindFolderInsertToPreparedStatement(PreparedStatement prepStmt, Folder folder) throws SQLException {
        prepStmt.setObject(1, folder.accountId());
        prepStmt.setString(2, folder.name());
        prepStmt.setString(3, folder.path());
        return prepStmt;
    }

    private Folder mapRowToFolder(ResultSet resultSet) throws SQLException {
        return new Folder(
                resultSet.getLong("id"),
                resultSet.getObject("account_id", java.util.UUID.class),
                resultSet.getString("name"),
                resultSet.getString("path")
        );
    }
}
