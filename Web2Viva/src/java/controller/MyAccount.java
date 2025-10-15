/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.City;
import hibernate.HibernateUtil;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "MyAccount", urlPatterns = {"/MyAccount"})
public class MyAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession ses = request.getSession(false);
        JsonObject responseObject = new JsonObject();

        if (ses != null && ses.getAttribute("user") != null) {
            User user = (User) ses.getAttribute("user");
            responseObject.addProperty("firstName", user.getFirstName());
            responseObject.addProperty("lastName", user.getLastName());
            responseObject.addProperty("email", user.getEmail());

            String since = new SimpleDateFormat("MMM yyyy").format(user.getCreated_at());
            responseObject.addProperty("since", since);

            Gson gson = new Gson();

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c = s.createCriteria(Address.class);
            c.add(Restrictions.eq("user", user));

            if (!c.list().isEmpty()) {
                System.out.println(c.list());
                List<Address> addressList = c.list();
                responseObject.add("addressList", gson.toJsonTree(addressList));
            }

            String toJson = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(toJson);

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 error
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject userData = gson.fromJson(request.getReader(), JsonObject.class);

        String firstName = userData.get("firstName").getAsString();
        String lastName = userData.get("lastName").getAsString();
        String email = userData.get("email").getAsString();
        String line1 = userData.get("line1").getAsString();
        String line2 = userData.get("line2").getAsString();
        int cityId = userData.get("cityId").getAsInt();
        String postalCode = userData.get("postalCode").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (firstName.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your First Name first!");
        } else if (lastName.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your Last Name!");
        } else if (email.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your Email Address!");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Invalid Email Address!");
        } else if (line1.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your Line 01!");
        } else if (line2.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your Line 02!");
        } else if (cityId == 0) {
            responseObject.addProperty("message", "Please Select City!");
        } else if (postalCode.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Postal code!");
        } else if (!Util.isPostalCodeValid(postalCode)) {
            responseObject.addProperty("message", "Invalid Postal Code!");
        } else {

            HttpSession ses = request.getSession();
            if (ses.getAttribute("user") != null) {

                User u = (User) ses.getAttribute("user");

                SessionFactory sf = HibernateUtil.getSessionFactory();
                Session s = sf.openSession();

                Criteria c = s.createCriteria(User.class);
                c.add(Restrictions.eq("email", u.getEmail()));

                User u1 = (User) c.list().get(0);

                u1.setFirstName(firstName);
                u1.setLastName(lastName);
                City city = (City) s.load(City.class, cityId);

                Criteria c1 = s.createCriteria(Address.class);
                c1.add(Restrictions.eq("user", u1));
                if (c1.list().isEmpty()) {

                    Address address1 = new Address();
                    address1.setLine1(line1);
                    address1.setLine2(line2);
                    address1.setCity(city);
                    address1.setPostalCode(postalCode);
                    address1.setUser(u1);
                    System.out.println("sd");

                    s.save(address1);

                } else {

                    Address address = (Address) c1.list().get(0);

//                    Address address = new Address();
                    address.setLine1(line1);
                    address.setLine2(line2);
                    address.setCity(city);
                    address.setPostalCode(postalCode);
                    address.setUser(u1);
                    address.setFirstName(firstName);
                    address.setLastName(lastName);
                    System.out.println("sdas");

                    s.update(address);
                }

                ses.setAttribute("user", u1);

                s.merge(u1);

                s.beginTransaction().commit();
                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Account Details Updated");

                s.close();

            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 error
            }

        }

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
