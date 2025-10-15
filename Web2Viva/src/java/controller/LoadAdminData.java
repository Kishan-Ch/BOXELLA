/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.HibernateUtil;
import hibernate.User;
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

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LoadAdminData", urlPatterns = {"/LoadAdminData"})
public class LoadAdminData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();

        responseObject.addProperty("status", false);
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        User user = (User) request.getSession().getAttribute("adminUser");

        if (user != null) {
            System.out.println(user.getFirstName());
            //category list
            Criteria c1 = s.createCriteria(Category.class);
            List<Category> categoryList = c1.list();

            user.setEmail(null);
            user.setPassword(null);
            user.setVerification(null);
            responseObject.addProperty("status", true);
            
            s.close();
            responseObject.add("userData", gson.toJsonTree(user));
            responseObject.add("categoryList", gson.toJsonTree(categoryList));

            String toJson = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(toJson);

        } else {
            System.out.println("Session Expired !");
        }

    }

}
