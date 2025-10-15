/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.HibernateUtil;
import hibernate.Item;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "SearchItems", urlPatterns = {"/SearchItems"})
public class SearchItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String searchText = request.getParameter("search");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        Gson gson = new Gson();
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        if (!searchText.isEmpty()) {
            Criteria c1 = s.createCriteria(Item.class);
            c1.add(Restrictions.ilike("name", "%" + searchText + "%"));

            List<Item> itemList = c1.list();

            responseObject.addProperty("status", true);
            responseObject.add("itemList", gson.toJsonTree(itemList));

        } else {
            responseObject.addProperty("message", "Search text is empty");
        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        s.close();
    }

}
