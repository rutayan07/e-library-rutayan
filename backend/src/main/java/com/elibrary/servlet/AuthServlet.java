package com.elibrary.servlet;

import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/api/auth")
public class AuthServlet extends HttpServlet {
    private Gson gson = new Gson();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        Map body = gson.fromJson(req.getReader(), Map.class);
        String role = (String) body.getOrDefault("role","guest");
        Map out = new HashMap(); out.put("role",role);
        if("student".equals(role)) { Map u = new HashMap(); u.put("id",1); u.put("name","Alice Sharma"); out.put("user", u); }
        resp.setContentType("application/json"); resp.getWriter().write(gson.toJson(out));
    }
}
