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
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "AddtoBox", urlPatterns = {"/AddtoBox"})
public class AddtoBox extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String prId = request.getParameter("id");
        String qty = request.getParameter("qty");
        String boxId = request.getParameter("boxId");
        String itemPrice = request.getParameter("itemPrice");
        String size = request.getParameter("size");

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (!Util.isInteger(prId)) {
            responseObject.addProperty("message", "Invalid product Id");
        } else if (!Util.isInteger(qty)) {
            responseObject.addProperty("message", "Invalid product Quantity");
        } else {

            //add to cart start
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            Transaction tr = s.beginTransaction();

            GiftBox giftBox = (GiftBox) s.get(GiftBox.class, Integer.valueOf(boxId));

            if (giftBox == null) {
                responseObject.addProperty("message", "Gift Box is not found!");
            } else {
                Item item = (Item) s.get(Item.class, Integer.valueOf(prId));
                if (item == null) {
                    responseObject.addProperty("message", "Product is not found!");
                } else {

                    // Calculate total amount for this product
                    double totalAmount = Integer.parseInt(qty) * Double.parseDouble(itemPrice);
                    System.out.println(totalAmount);
                    responseObject.addProperty("totalAmount", totalAmount);

                    User user = (User) request.getSession().getAttribute("user");

                    if (user != null) { // add to db cart 

                        Criteria c1 = s.createCriteria(Cart.class);
                        c1.add(Restrictions.eq("user", user));
                        c1.add(Restrictions.eq("giftBox", giftBox));

                        if (c1.list().isEmpty()) {

                            Cart cart = new Cart();
                            cart.setGiftBox(giftBox);
                            cart.setUser(user);
                            cart.setStatus(1);
                            cart.setAdded_at(new Date());

                            CartItem cartItem = new CartItem();
                            cartItem.setCart(cart);
                            cartItem.setItem(item);
                            cartItem.setQty(Integer.parseInt(qty));

                            s.save(cart);
                            s.save(cartItem);
                            tr.commit();

                            responseObject.addProperty("status", true);
                            responseObject.addProperty("message", "Product Added to the Box");

                        } else {

                            Cart cart1 = (Cart) c1.list().get(0);
                            Criteria c2 = s.createCriteria(CartItem.class);
                            c2.add(Restrictions.eq("cart", cart1));

                            if (c2.list().isEmpty()) {

                                CartItem cartItem = new CartItem();
                                cartItem.setCart(cart1);
                                cartItem.setItem(item);
                                cartItem.setQty(Integer.parseInt(qty));

                                s.save(cartItem);
                                tr.commit();

                                responseObject.addProperty("status", true);
                                responseObject.addProperty("message", "Cart Updated");

                            } else {
                                if (Integer.parseInt(qty) <= item.getQty()) { // product quantity available

                                    Criteria c3 = s.createCriteria(CartItem.class);
                                    c3.add(Restrictions.eq("item", item));

                                    if (c3.list().isEmpty()) {

                                        CartItem cartItem = new CartItem();
                                        cartItem.setCart(cart1);
                                        cartItem.setItem(item);
                                        cartItem.setQty(Integer.parseInt(qty));

                                        s.save(cartItem);
                                        tr.commit();

                                        responseObject.addProperty("status", true);
                                        responseObject.addProperty("message", "Product Added to the Box");

                                    } else {

                                        CartItem cartItem = (CartItem) c3.list().get(0);

                                        int newQty = cartItem.getQty() + Integer.parseInt(qty);
                                        if (newQty <= item.getQty()) {

                                            cartItem.setQty(newQty);
                                            s.update(cartItem);
                                            tr.commit();

                                            responseObject.addProperty("status", true);
                                            responseObject.addProperty("message", "Cart Updated");

                                        } else {
                                            responseObject.addProperty("message", "Insufficient Product Quantity!");
                                        }
                                    }
                                    responseObject.addProperty("status", true);
                                    responseObject.addProperty("message", "Product Added to the Box");
                                } else {
                                    responseObject.addProperty("message", "Insufficient Product Quantity!");
                                }
                            }

                        }

                    } else {// add to session cart

                        HttpSession ses = request.getSession();

                        if (ses.getAttribute("sessionBox") == null) {

                            if (Integer.parseInt(qty) <= item.getQty()) {

                                ArrayList<CartItem> sesItemBox = new ArrayList<>();
                                CartItem cartItem = new CartItem();
                                cartItem.setQty(Integer.parseInt(qty));
                                cartItem.setItem(item);
                                sesItemBox.add(cartItem);

                                ArrayList<Cart> sesCart = new ArrayList<>();
                                Cart cart = new Cart();
                                cart.setGiftBox(giftBox);
                                cart.setStatus(1);
                                cart.setUser(null);
                                sesCart.add(cart);

                                ses.setAttribute("sesItemBox", sesItemBox);
                                ses.setAttribute("sessionCart", sesCart);

                                responseObject.addProperty("status", true);
                                responseObject.addProperty("message", "product added to the box");
                            } else {
                                responseObject.addProperty("message", "Insufficient Product Quantity!");
                            }
                        } else {
                            ArrayList<CartItem> sessList = new ArrayList<>();
                            CartItem foundedItemBox = null;
                            Cart foundedCart = null;

                            for (CartItem cartItem : sessList) {
                                if (cartItem.getItem().getId() == item.getId()) {
                                    foundedItemBox = cartItem;
                                    break;
                                }
                            }

                            if (foundedItemBox != null) {
                                int newQty = foundedItemBox.getQty() + Integer.parseInt(qty);
                                if (newQty <= item.getQty()) {

                                    foundedItemBox.setQty(newQty);
                                    responseObject.addProperty("status", true);
                                    responseObject.addProperty("message", "Box Upated");

                                    totalAmount = newQty * Double.parseDouble(itemPrice);
                                    responseObject.addProperty("totalAmount", totalAmount);

                                } else {
                                    responseObject.addProperty("message", "Insufficient Product Quantity!");
                                }

                            } else {
                                foundedItemBox = new CartItem();
                                foundedCart = new Cart();

                                if (Integer.parseInt(qty) <= item.getQty()) {

                                    foundedItemBox.setQty(Integer.parseInt(qty));
                                    foundedCart.setUser(null);
                                    foundedItemBox.setItem(item);
                                    foundedCart.setGiftBox(giftBox);
                                    sessList.add(foundedItemBox);
                                    responseObject.addProperty("status", true);
                                    responseObject.addProperty("message", "product added to the box");
                                } else {
                                    responseObject.addProperty("message", "Insufficient Product Quantity!");
                                }

                            }
                        }

                    }
                }
            }

        }
        responseObject.addProperty("status", true);

        //add to cart end
        String toJson = gson.toJson(responseObject);

        response.setContentType(
                "application/json");
        response.getWriter()
                .write(toJson);
    }

}
