/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import hibernate.GiftBox;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LoadBoxData", urlPatterns = {"/LoadBoxData"})
public class LoadBoxData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();

        Gson gson = new Gson();

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Criteria c = s.createCriteria(GiftBox.class);
        List<GiftBox> boxList = c.list();
        if (!c.list().isEmpty()) {

            responseObject.add("boxList", gson.toJsonTree(boxList));

        } else {
            responseObject.addProperty("message", "Something went wrong! Please try again later!");
        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
