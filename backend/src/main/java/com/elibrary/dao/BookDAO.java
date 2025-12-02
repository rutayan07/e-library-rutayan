package com.elibrary.dao;

import com.elibrary.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<Book> findAll() throws SQLException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT id,title,author,isbn,copies,description FROM books ORDER BY title";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                list.add(new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6)));
            }
        }
        return list;
    }

    public Book findById(int id) throws SQLException{
        String sql = "SELECT id,title,author,isbn,copies,description FROM books WHERE id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id); try(ResultSet rs = ps.executeQuery()){ if(rs.next()) return new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6)); }
        }
        return null;
    }

    public int insert(Book b) throws SQLException{
        String sql = "INSERT INTO books(title,author,isbn,copies,description) VALUES(?,?,?,?,?)";
        try(Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,b.getTitle());ps.setString(2,b.getAuthor());ps.setString(3,b.getIsbn());ps.setInt(4,b.getCopies());ps.setString(5,b.getDescription());
            ps.executeUpdate(); try(ResultSet rs = ps.getGeneratedKeys()){ if(rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean update(Book b) throws SQLException{
        String sql = "UPDATE books SET title=?,author=?,isbn=?,copies=?,description=? WHERE id=?";
        try(Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,b.getTitle());ps.setString(2,b.getAuthor());ps.setString(3,b.getIsbn());ps.setInt(4,b.getCopies());ps.setString(5,b.getDescription());ps.setInt(6,b.getId());
            return ps.executeUpdate()>0;
        }
    }

    public boolean delete(int id) throws SQLException{
        String sql = "DELETE FROM books WHERE id=?";
        try(Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id); return ps.executeUpdate()>0;
        }
    }
}
