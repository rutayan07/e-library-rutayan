package com.elibrary.dao;

import com.elibrary.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> findAll() throws SQLException{
        List<User> list = new ArrayList<>();
        String sql = "SELECT id,name,email,role FROM users ORDER BY name";
        try(Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while(rs.next()) list.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
        }
        return list;
    }

    public User findById(int id) throws SQLException{
        String sql = "SELECT id,name,email,role FROM users WHERE id=?";
        try(Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id); try(ResultSet rs = ps.executeQuery()){ if(rs.next()) return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)); }
        }
        return null;
    }

    public int insert(User u) throws SQLException{
        String sql = "INSERT INTO users(name,email,role) VALUES(?,?,?)";
        try(Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,u.getName());ps.setString(2,u.getEmail());ps.setString(3,u.getRole()); ps.executeUpdate(); try(ResultSet rs = ps.getGeneratedKeys()){ if(rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public boolean update(User u) throws SQLException{
        String sql = "UPDATE users SET name=?,email=?,role=? WHERE id=?";
        try(Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,u.getName());ps.setString(2,u.getEmail());ps.setString(3,u.getRole());ps.setInt(4,u.getId()); return ps.executeUpdate()>0;
        }
    }

    public boolean delete(int id) throws SQLException{
        String sql = "DELETE FROM users WHERE id=?";
        try(Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id); return ps.executeUpdate()>0;
        }
    }
}
