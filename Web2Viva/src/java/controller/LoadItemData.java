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
import hibernate.Status;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LoadItemData", urlPatterns = {"/LoadItemData"})
public class LoadItemData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        //category list
        Criteria c1 = s.createCriteria(Category.class);
        List<Category> categoryList = c1.list();

        Status status = (Status) s.get(Status.class, 1);

        Criteria c = s.createCriteria(Item.class);

        c.addOrder(Order.desc("id"));
        c.add(Restrictions.eq("status", status));

        List<Item> itemList = c.list();
        for (Item item : itemList) {
            item.setUser(null);
        }
        Gson gson = new Gson();

        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        responseObject.addProperty("productCount", c.list().size());
        responseObject.add("itemList", gson.toJsonTree(itemList));

        responseObject.addProperty("status", true);
        
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        s.close();
    }

}
