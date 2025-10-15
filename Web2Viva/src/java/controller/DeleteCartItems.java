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
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "DeleteCartItems", urlPatterns = {"/DeleteCartItems"})
public class DeleteCartItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String prId = request.getParameter("id");
        String boxId = request.getParameter("boxId");

        Gson gson = new Gson();
        JsonObject responseObejct = new JsonObject();
        responseObejct.addProperty("status", false);

        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            Transaction tr = s.beginTransaction();

            GiftBox giftBox = (GiftBox) s.get(GiftBox.class, Integer.valueOf(boxId));
            Item item = (Item) s.get(Item.class, Integer.valueOf(prId));

            Criteria c1 = s.createCriteria(Cart.class);
            c1.add(Restrictions.eq("user", user));
            c1.add(Restrictions.eq("giftBox", giftBox));

            Cart cart = (Cart) c1.uniqueResult();
            if (cart != null) {

                Criteria c2 = s.createCriteria(CartItem.class);
                c2.add(Restrictions.eq("cart", cart));
                c2.add(Restrictions.eq("item", item));

                if (!c2.list().isEmpty()) {
                    List<CartItem> CartItems = c2.list();

                    for (CartItem cartItem1 : CartItems) {
                        s.delete(cartItem1);
                    }

                } else {
                    responseObejct.addProperty("message", "Something went wrong. Please try again");
                }

            }

            tr.commit();
            responseObejct.addProperty("status", true);
            responseObejct.addProperty("message", "Item Deleted");
            s.close();

        } else {
            responseObejct.addProperty("message", "no-session");
        }
        
        String toJson = gson.toJson(responseObejct);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
