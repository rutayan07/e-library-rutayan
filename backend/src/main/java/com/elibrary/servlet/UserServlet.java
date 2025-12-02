package com.elibrary.servlet;

import com.elibrary.dao.UserDAO;
import com.elibrary.model.User;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/api/users/*")
public class UserServlet extends HttpServlet {
    private UserDAO dao = new UserDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        try{
            List<User> list = dao.findAll(); resp.setContentType("application/json"); resp.getWriter().write(gson.toJson(list));
        } catch(SQLException e){ throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        User u = gson.fromJson(req.getReader(), User.class);
        try{ if(u.getId()<=0){ dao.insert(u); } else dao.update(u); resp.setContentType("application/json"); resp.getWriter().write(gson.toJson(u)); } catch(SQLException e){ throw new ServletException(e); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        try{ int id = Integer.parseInt(req.getPathInfo().substring(1)); dao.delete(id); resp.setStatus(HttpServletResponse.SC_NO_CONTENT);} catch(SQLException e){ throw new ServletException(e); }
    }
}
