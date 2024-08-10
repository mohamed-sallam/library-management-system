package com.ebi.library_management_system.db;

import com.ebi.library_management_system.domain.Authors;
import com.ebi.library_management_system.entity.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorsRepository implements Authors {
  private final Connection connection;

  @Override
  public Author getById(int id) {
    try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM authors WHERE id = ? LIMIT 1")) {
      stmt.setInt(1, id);
      try (ResultSet res = stmt.executeQuery()) {
        if (!res.next())
          return null;
        return new Author(res.getInt("id"), res.getString("full_name"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
