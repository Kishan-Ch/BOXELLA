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
@WebServlet(name = "SearchProduct", urlPatterns = {"/SearchProduct"})
public class SearchProduct extends HttpServlet {

    public static final int MAX_RESULT = 4;
    public static final int ACTIVE_ID = 1;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Criteria c1 = s.createCriteria(Item.class);

        if (requestJsonObject.has("categoryName")) {
            String categoryName = requestJsonObject.get("categoryName").getAsString();
            Criteria c2 = s.createCriteria(Category.class);
            c2.add(Restrictions.eq("name", categoryName));
            Category category = (Category) c2.uniqueResult();
            if (category != null) {
                c1.add(Restrictions.eq("category", category));
                System.out.println(category);

            } else {
                responseObject.addProperty("message", "Products not found!");
            }
        }


        if (requestJsonObject.has("priceStart") && requestJsonObject.has("priceEnd")) {
            double priceStart = requestJsonObject.get("priceStart").getAsDouble();
            double priceEnd = requestJsonObject.get("priceEnd").getAsDouble();

            c1.add(Restrictions.ge("price", priceStart));
            c1.add(Restrictions.le("price", priceEnd));

        }

        if (requestJsonObject.has("sortValue")) {
            String sortValue = requestJsonObject.get("sortValue").getAsString();

            if (sortValue.equals("Sort by Latest")) {
                c1.addOrder(Order.desc("id"));
            } else if (sortValue.equals("Sort by Oldest")) {
                c1.addOrder(Order.asc("id"));
            } else if (sortValue.equals("Sort by Name")) {
                c1.addOrder(Order.asc("name"));
            } else if (sortValue.equals("Sort by Price")) {
                c1.addOrder(Order.asc("price"));
            }
        }
        
        
        System.out.println(c1.list().size());
        responseObject.addProperty("productCount", c1.list().size());

        if (requestJsonObject.has("firstResult")) {
            int firstResult = requestJsonObject.get("firstResult").getAsInt();
            c1.setFirstResult(firstResult);
            c1.setMaxResults(SearchProduct.MAX_RESULT);
        }

        Status status = (Status) s.get(Status.class, ACTIVE_ID);
        c1.add(Restrictions.eq("status", status));
        List<Item> itemList = c1.list();

        s.close();

        responseObject.add("itemList", gson.toJsonTree(itemList));
        responseObject.addProperty("status", true);
        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

}
