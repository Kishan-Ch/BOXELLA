/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.GiftBox;
import hibernate.HibernateUtil;
import hibernate.Item;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        String productId = request.getParameter("id");
        String boxId = request.getParameter("boxId");

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        if (productId != null) {
            if (Util.isInteger(productId)) {

                try {
                    Item item = (Item) s.get(Item.class, Integer.valueOf(productId));
                    if (item.getStatus().getName().equals("Active")) {

                        item.getUser().setEmail(null);
                        item.getUser().setPassword(null);
                        item.getUser().setVerification(null);
                        item.getUser().setCreated_at(null);
                        item.getUser().setUserType(null);

                        responseObject.add("item", gson.toJsonTree(item));
                        responseObject.addProperty("status", true);

                    } else {
                        responseObject.addProperty("message", "Product not Found!");
                    }

                } catch (Exception e) {
                    responseObject.addProperty("message", "Product not Found!");
                }
            } else {
                System.out.println("a");
            }
        } else if (boxId != null) {

            if (Util.isInteger(boxId)) {
                try {

                    GiftBox giftBox = (GiftBox) s.get(GiftBox.class, Integer.valueOf(boxId));

                    responseObject.add("giftBox", gson.toJsonTree(giftBox));
                    responseObject.addProperty("status", true);
                    
                } catch (Exception e) {
                    responseObject.addProperty("message", "Product not Found!");
                }
            }

        }

        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);
    }

}
