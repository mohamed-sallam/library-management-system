package com.ebi.library_management_system.db;

import com.ebi.library_management_system.domain.Authors;
import com.ebi.library_management_system.domain.Books;
import com.ebi.library_management_system.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcBooksRepository implements Books {
    private final Authors authorsRepo;
    private final Connection connection;

    @Override
    public void add(Book book) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO library_db.books (title, author_id, published_at) VALUES ( ? , ? , ? )")) {
            stmt.setString(1, book.title());
            stmt.setInt(2, book.author().id());
            stmt.setDate(3, book.publishedAt());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> getAll() {
        try (Statement stmt = connection.createStatement(); ResultSet res = stmt.executeQuery("SELECT * FROM library_db.books")) {
            List<Book> books = new ArrayList<>(res.getFetchSize());
            while (res.next())
                books.add(new Book(res.getInt("id"), res.getString("title"), authorsRepo.getById(res.getInt("author_id")), res.getDate("published_at")));
            return books;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(Book book) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM library_db.books WHERE id = ?")) {
            stmt.setInt(1, book.id());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> getByTitle(String title) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM library_db.books WHERE title LIKE ?")) {
            stmt.setString(1, "%" + title + "%");
            try (ResultSet res = stmt.executeQuery()) {
                List<Book> books = new ArrayList<>(res.getFetchSize());
                while (res.next())
                    books.add(new Book(res.getInt("id"), res.getString("title"), authorsRepo.getById(res.getInt("author_id")), res.getDate("published_at")));
                return books;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> getByAuthor(String author) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM library_db.books WHERE author_id IN (SELECT id FROM library_db.authors WHERE full_name LIKE ? )")) {
            stmt.setString(1, "%" + author + "%");
            try (ResultSet res = stmt.executeQuery()) {
                List<Book> books = new ArrayList<>(res.getFetchSize());
                while (res.next())
                    books.add(new Book(res.getInt("id"), res.getString("title"), authorsRepo.getById(res.getInt("author_id")), res.getDate("published_at")));
                return books;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book getById(int id) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM library_db.books WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet res = stmt.executeQuery()) {
                if (!res.next()) return null;
                return new Book(res.getInt("id"), res.getString("title"), authorsRepo.getById(res.getInt("author_id")), res.getDate("published_at"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
