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
import hibernate.Item;
import hibernate.OrderItems;
import hibernate.OrderStatus;
import hibernate.Orders;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.PayHere;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    private static final int SELECTOR_DEFAULT_VALUE = 0;
    private static final int ORDER_PENDING = 5;
//    private static final int ORDER_PENDING = 5;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject resJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        boolean isCurrentAddress = resJsonObject.get("isCurrentAddress").getAsBoolean();
        String firstName = resJsonObject.get("firstName").getAsString();
        String lastName = resJsonObject.get("lastName").getAsString();
        String citySelect = resJsonObject.get("citySelect").getAsString();
        String lineOne = resJsonObject.get("lineOne").getAsString();
        String lineTwo = resJsonObject.get("lineTwo").getAsString();
        String mobile = resJsonObject.get("mobile").getAsString();
        String postalCode = resJsonObject.get("postalCode").getAsString();
        String amount = resJsonObject.get("amount").getAsString();
        String boxId = resJsonObject.get("boxId").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            responseObject.addProperty("message", "Session Expired. Please sign in again");
        } else {
            if (isCurrentAddress) {
                Criteria c1 = s.createCriteria(Address.class);
                c1.add(Restrictions.eq("user", user));
                c1.addOrder(Order.desc("id"));
                if (c1.list().isEmpty()) {
                    responseObject.addProperty("message", "Yoy current address is not found. Please add a new Address");
                } else {
                    Address address = (Address) c1.list().get(0);
                    processCheckout(s, tr, user, address, responseObject, amount, boxId);
                }
            } else {

                if (firstName.isEmpty()) {
                    responseObject.addProperty("message", "Please Enter your First Name first!");
                } else if (lastName.isEmpty()) {
                    responseObject.addProperty("message", "Please Enter your Last Name!");
                } else if (!Util.isInteger(citySelect)) {
                    responseObject.addProperty("message", "Invalid city!");
                } else if (Integer.parseInt(citySelect) == Checkout.SELECTOR_DEFAULT_VALUE) {
                    responseObject.addProperty("message", "Please Select City!");
                } else {
                    City city = (City) s.get(City.class, Integer.valueOf(citySelect));
                    if (city == null) {
                        responseObject.addProperty("message", "Invalid City name!");
                    } else {
                        if (lineOne.isEmpty()) {
                            responseObject.addProperty("message", "Please Enter your Line 01!");
                        } else if (lineTwo.isEmpty()) {
                            responseObject.addProperty("message", "Please Enter your Line 02!");
                        } else if (postalCode.isEmpty()) {
                            responseObject.addProperty("message", "Please Enter Postal code!");
                        } else if (!Util.isPostalCodeValid(postalCode)) {
                            responseObject.addProperty("message", "Invalid Postal Code!");
                        } else if (mobile.isEmpty()) {
                            responseObject.addProperty("message", "Please Enter Mobile Number!");
                        } else if (!Util.isValidMobile(mobile)) {
                            responseObject.addProperty("message", "Invalid Mobile Number!");
                        } else {

                            Address address = new Address();
                            address.setLine1(lineOne);
                            address.setLine2(lineTwo);
                            address.setCity(city);
                            address.setMobile(mobile);
                            address.setPostalCode(postalCode);
                            address.setUser(user);
                            address.setFirstName(firstName);
                            address.setLastName(lastName);
                            s.save(address);

                            processCheckout(s, tr, user, address, responseObject, amount, boxId);

                        }

                    }

                }

            }

        }

        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);

    }

    private void processCheckout(Session s, Transaction tr, User user, Address address, JsonObject responseObject, String amount, String boxId) {

        try {
            Orders orders = new Orders();
            orders.setAddress(address);
            orders.setCreated_at(new Date());
            orders.setUser(user);

            int orderId = (int) s.save(orders);

            GiftBox box = (GiftBox) s.get(GiftBox.class, Integer.parseInt(boxId));

            Criteria c1 = s.createCriteria(Cart.class);
            c1.add(Restrictions.eq("user", user));
            c1.add(Restrictions.eq("giftBox", box));
            c1.add(Restrictions.eq("status", 1));

            List<Cart> cart = c1.list();
            OrderStatus orderStatus = (OrderStatus) s.get(OrderStatus.class, Checkout.ORDER_PENDING);

            for (Cart cart1 : cart) {
                Criteria c2 = s.createCriteria(CartItem.class);
                c2.add(Restrictions.eq("cart", cart1));

                List<CartItem> cartItems1 = c2.list();

                for (CartItem cartItem : cartItems1) {

                    OrderItems orderItems = new OrderItems();
                    orderItems.setOrders(orders);
                    orderItems.setCartItem(cartItem);
                    orderItems.setOrderStatus(orderStatus);
                    orderItems.setQty(cartItem.getQty());

                    s.save(orderItems);

                    Item item = cartItem.getItem();
                    if (item != null) {
                        int newQty = item.getQty() - cartItem.getQty();
                        if (newQty < 0) {
                            newQty = 0;
                        }
                        item.setQty(newQty);
                        s.update(item);
                    }

                }

                cart1.setStatus(0);
                s.update(cart1);

            }

            tr.commit();

            String amountStr = amount.replace(",", "");
            System.out.println(amountStr);
//            

//            //payhere process
            String merahantID = "1227033";
            String merchantSecret = "MzczMjgwNzc1NDE5NzM3NzU2NjQwMTk2MzEzOTMyMDkxMDQxNTI2";
            String orderID = "#000" + orderId;
            String currency = "LKR";
            DecimalFormat df = new DecimalFormat("0.00");
            String amountFormatted = df.format(Double.parseDouble(amountStr));
            String merchantSecretMD5 = PayHere.generateMD5(merchantSecret);
            String hash = PayHere.generateMD5(merahantID + orderID + amountFormatted + currency + merchantSecretMD5);

            JsonObject payHereJson = new JsonObject();
            payHereJson.addProperty("sandbox", true);
            payHereJson.addProperty("merchant_id", merahantID);

            payHereJson.addProperty("return_url", "");
            payHereJson.addProperty("cancel_url", "");
            payHereJson.addProperty("notify_url", " https://9567371a1c1c.ngrok-free.app/Web2Viva/VerifyPayments");

            payHereJson.addProperty("order_id", orderID);
            payHereJson.addProperty("items", box.getName());
            payHereJson.addProperty("amount", amountFormatted);
            payHereJson.addProperty("currency", currency);
            payHereJson.addProperty("hash", hash);

            payHereJson.addProperty("first_name", user.getFirstName());
            payHereJson.addProperty("last_name", user.getLastName());
            payHereJson.addProperty("email", user.getEmail());

            payHereJson.addProperty("phone", address.getMobile());
            payHereJson.addProperty("address", address.getLine1() + ", " + address.getLine2());
            payHereJson.addProperty("city", address.getCity().getName());
            payHereJson.addProperty("country", "Sri Lanka");

            responseObject.addProperty("message", "Order Successful");
            responseObject.addProperty("status", true);
            responseObject.add("payhereJson", new Gson().toJsonTree(payHereJson));

        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        }

    }

}
