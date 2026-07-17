package app.markdownotes.repository;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class JdbcPipeline {

    @FunctionalInterface
    interface JdbcConsumer<T> {
        void accept(T t) throws SQLException;
    }

    @FunctionalInterface
    interface JdbcBiConsumer<T, U> {
        void accept(T t, U u) throws SQLException;
    }

    @FunctionalInterface
    interface JdbcFunction<T, R> {
        R apply(T t) throws SQLException;
    }

    @Inject
    AgroalDataSource dataSource;

    public <T> T executeQuery(String sql, JdbcConsumer<PreparedStatement> prepStmtConsumer, JdbcFunction<ResultSet, T> resultSetMapper) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmtConsumer.accept(prepStmt);
            try (ResultSet resultSet = prepStmt.executeQuery()) {
                return resultSetMapper.apply(resultSet);
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error executing query", ex);
        }
    }

    public int executeUpdate(String sql, JdbcConsumer<PreparedStatement> prepStmtConsumer) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql)) {

            prepStmtConsumer.accept(prepStmt);
            return prepStmt.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException("Error executing query", ex);
        }
    }

}
