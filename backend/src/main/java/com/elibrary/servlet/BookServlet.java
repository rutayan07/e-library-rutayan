package com.elibrary.servlet;

import com.elibrary.dao.BookDAO;
import com.elibrary.model.Book;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = "/api/books/*")
public class BookServlet extends HttpServlet {
    private BookDAO dao = new BookDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        try{
            String path = req.getPathInfo();
            if(path==null || "/".equals(path)){
                List<Book> list = dao.findAll();
                resp.setContentType("application/json"); resp.getWriter().write(gson.toJson(list));
            } else {
                int id = Integer.parseInt(path.substring(1));
                Book b = dao.findById(id);
                resp.setContentType("application/json"); resp.getWriter().write(gson.toJson(b));
            }
        } catch(SQLException e){ throw new ServletException(e); }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Book b = gson.fromJson(req.getReader(), Book.class);
        try{
            if(b.getId()<=0){ int newId = dao.insert(b); b.setId(newId); }
            else dao.update(b);
            resp.setContentType("application/json"); resp.getWriter().write(gson.toJson(b));
        } catch(SQLException e){ throw new ServletException(e); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        try{
            String path = req.getPathInfo(); int id = Integer.parseInt(path.substring(1));
            dao.delete(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch(SQLException e){ throw new ServletException(e); }
    }
}
