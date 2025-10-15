/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Cart;
import hibernate.CartItem;
import hibernate.GiftBox;
import hibernate.HibernateUtil;
import hibernate.Item;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
@WebServlet(name = "LoadCartItems", urlPatterns = {"/LoadCartItems"})
public class LoadCartItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseObejct = new JsonObject();
        responseObejct.addProperty("status", false);

        String boxId = request.getParameter("boxId");

        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            GiftBox giftBox = (GiftBox) s.get(GiftBox.class, Integer.valueOf(boxId));

            Criteria c1 = s.createCriteria(Cart.class);
            c1.add(Restrictions.eq("user", user));
            c1.add(Restrictions.eq("giftBox", giftBox));
            c1.add(Restrictions.eq("status", 1));

            Cart cart = (Cart) c1.uniqueResult();
            if (cart != null) {

                Criteria c2 = s.createCriteria(CartItem.class);
                c2.add(Restrictions.eq("cart", cart));
                List<CartItem> CartItems = c2.list();

                List<Cart> cartList = c1.list();

                for (Cart cart1 : cartList) {
                    cart1.setUser(null);
                }
                responseObejct.addProperty("status", true);
                responseObejct.addProperty("message", "Cart Item Loaded");
                responseObejct.add("cartItems", gson.toJsonTree(CartItems));

            } else {
                responseObejct.addProperty("message", "Cart is Empty!!");
            }

        } else {//session cart

            ArrayList<CartItem> sessionCarts = (ArrayList<CartItem>) request.getSession().getAttribute("sesItemBox");
            if (sessionCarts != null) {
                if (sessionCarts.isEmpty()) {
                    responseObejct.addProperty("message", "Cart is Empty!");
                } else {


                    responseObejct.addProperty("status", true);
                    responseObejct.addProperty("message", "Cart Item Loaded");
                    responseObejct.add("cartItems", gson.toJsonTree(sessionCarts));
                }
            } else {
                responseObejct.addProperty("message", "Cart is Empty!");
            }

        }

        String toJson = gson.toJson(responseObejct);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
