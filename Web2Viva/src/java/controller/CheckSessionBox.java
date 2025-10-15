/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import hibernate.Cart;
import hibernate.CartItem;
import hibernate.HibernateUtil;
import hibernate.Item;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
@WebServlet(name = "CheckSessionBox", urlPatterns = {"/CheckSignIn"})
public class CheckSessionBox extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//       
        User user = (User) request.getAttribute("user");
        if (user != null) {

            ArrayList<CartItem> sessionBoxes = (ArrayList<CartItem>) request.getSession().getAttribute("sesItemBox");
            if (sessionBoxes != null && !sessionBoxes.isEmpty()) {

                SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();
                Transaction tr = s.beginTransaction();

                try {
                    for (CartItem sessBox : sessionBoxes) {

                        Item item = (Item) s.get(Item.class, sessBox.getItem().getId());

                        // Create a new Criteria for each item
                        Criteria c1 = s.createCriteria(CartItem.class);
                        c1.add(Restrictions.eq("user", user));
                        c1.add(Restrictions.eq("item", sessBox.getItem()));

                        CartItem dbBox = (CartItem) c1.uniqueResult();

                        if (dbBox == null) {
//                            sessBox.setUser(user);
                            s.save(sessBox);
                        } else {
                            int newQty = sessBox.getQty() + dbBox.getQty();
                            if (newQty <= item.getQty()) {
                                dbBox.setQty(newQty);
//                                dbBox.setUser(user);
                                s.update(dbBox);
                            }
                        }
                    }

                    tr.commit();

                    // Clear session cart after saving
                    request.getSession().setAttribute("sesItemBox", null);

                } catch (Exception e) {
                    tr.rollback();
                    e.printStackTrace();
                } finally {
                    s.close();
                }

            }

        } else {
            System.out.println("sign in plz");
        }

    }

}
