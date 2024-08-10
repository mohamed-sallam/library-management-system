package com.ebi.library_management_system.db;

import com.ebi.library_management_system.domain.Books;
import com.ebi.library_management_system.domain.Copies;
import com.ebi.library_management_system.entity.Book;
import com.ebi.library_management_system.entity.Copy;
import com.ebi.library_management_system.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcCopiesRepository implements Copies {
    private final Books booksRepo;
    private final Connection connection;

    @Override
    public List<Copy> getAvailable(Book book) {
        try (PreparedStatement stmt = connection.prepareStatement("""
                SELECT * FROM copies WHERE book_id = ? AND NOT EXISTS(
                    SELECT * FROM borrow WHERE copy_id=copies.id AND NOT EXISTS(
                        SELECT * FROM `return` WHERE borrow_id=borrow.id
                    )
                )
                """)) {
            stmt.setInt(1, book.id());
            try (ResultSet res = stmt.executeQuery()) {
                List<Copy> copies = new ArrayList<>(res.getFetchSize());
                while (res.next())
                    copies.add(new Copy(res.getInt("id"), booksRepo.getById(res.getInt("book_id")), res.getDate("added_date")));
                return copies;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void borrow(Customer customer, Copy copy) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO borrow (customer_id, copy_id, borrowed_at) VALUES ( ? , ? ,CURDATE())")) {
            stmt.setInt(1, customer.id());
            stmt.setInt(2, copy.id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean returnCopy(Customer customer, Copy copy) {
        int borrowId = -1;
        try (PreparedStatement stmt = connection.prepareStatement("""
                SELECT id FROM borrow WHERE copy_id = ? AND customer_id = ? AND NOT EXISTS(
                    SELECT * FROM `return` WHERE borrow_id=borrow.id
                )
                """)) {
            stmt.setInt(1, copy.id());
            stmt.setInt(2, customer.id());
            try (ResultSet res = stmt.executeQuery()) {
                if (!res.next()) return false;
                borrowId = res.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement stmt = connection.prepareStatement("""
                  INSERT INTO `return` (borrow_id, returned_at) VALUES ( ? ,CURDATE())
                """)) {
            stmt.setInt(1, borrowId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Copy> getBorrowed(Customer customer) {
        try (PreparedStatement stmt = connection.prepareStatement("""
                SELECT * FROM copies WHERE EXISTS(
                    SELECT * FROM borrow WHERE customer_id = ? AND copy_id=copies.id AND NOT EXISTS(
                        SELECT * FROM `return` WHERE borrow_id=borrow.id
                    )
                )
                """)) {
            stmt.setInt(1, customer.id());
            try (ResultSet res = stmt.executeQuery()) {
                List<Copy> copies = new ArrayList<>(res.getFetchSize());
                while (res.next())
                    copies.add(new Copy(res.getInt("id"), booksRepo.getById(res.getInt("book_id")), res.getDate("added_date")));
                return copies;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
