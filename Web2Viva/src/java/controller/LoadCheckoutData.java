/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.Cart;
import hibernate.CartItem;
import hibernate.City;
import hibernate.GiftBox;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "LoadCheckoutData", urlPatterns = {"/LoadCheckoutData"})
public class LoadCheckoutData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String boxId = request.getParameter("boxId");
        
        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        User sessionUser = (User) request.getSession().getAttribute("user");

        if (sessionUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 error
        } else {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c1 = s.createCriteria(Address.class);
            c1.add(Restrictions.eq("user", sessionUser));
            c1.addOrder(Order.desc("id"));
            List<Address> addressList = c1.list();
            if (addressList.isEmpty()) {
                responseObject.addProperty("message", "Account details are incomplete. Please fill your Address details");
            } else {
                Address address = (Address) c1.list().get(0);
                address.getUser().setEmail(null);
                address.getUser().setPassword(null);
                address.getUser().setVerification(null);
                address.getUser().setCreated_at(null);
                address.getUser().setUserType(null);
                responseObject.add("userAddress", gson.toJsonTree(address));
            }

            Criteria c2 = s.createCriteria(City.class);
            c2.addOrder(Order.asc("name"));
            List<City> citylist = c2.list();
            responseObject.add("cityList", gson.toJsonTree(citylist));

            GiftBox giftBox = (GiftBox) s.get(GiftBox.class, Integer.valueOf(boxId));
            responseObject.add("giftBox", gson.toJsonTree(giftBox));
            
            
            Criteria c3 = s.createCriteria(Cart.class);
            c3.add(Restrictions.eq("user", sessionUser));
            c3.add(Restrictions.eq("giftBox", giftBox));

            Cart cart = (Cart) c3.uniqueResult();
            if (cart != null) {

                Criteria c4 = s.createCriteria(CartItem.class);
                c4.add(Restrictions.eq("cart", cart));
                List<CartItem> CartItems = c4.list();

                List<Cart> cartList = c3.list();

                for (Cart cart1 : cartList) {
                    cart1.setUser(null);
                }
                responseObject.add("cartItems", gson.toJsonTree(CartItems));

            } else {
                responseObject.addProperty("message", "empty-cart");
            }
            s.close();
            
        }

        responseObject.addProperty("status", true);

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
    }

}
