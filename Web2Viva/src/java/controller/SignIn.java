/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.UserType;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        String email = user.get("email").getAsString();
        String password = user.get("password").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (email.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your Email Address!");
        } else if (!Util.isEmailValid(email)) {
            responseObject.addProperty("message", "Invalid Email Address!");
        } else if (password.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your Password!");
        } else if (!Util.isPasswordValid(password)) {
            responseObject.addProperty("message", "The Password must contains at least uppercase, lowercase, number, special character and to be minimum 8 characters long!");
        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c1 = s.createCriteria(User.class);
            c1.add(Restrictions.eq("email", email));
            c1.add(Restrictions.eq("password", password));

            if (c1.list().isEmpty()) {
                responseObject.addProperty("message", "Invalid credentials!");
                responseObject.addProperty("status", false);

            } else {

                User u = (User) c1.list().get(0);
                HttpSession ses = request.getSession();

                if (u.getUserType().getId() == 1) {
                    responseObject.addProperty("message", "Admin");
                    ses.setAttribute("adminUser", u);
                } else {
                    ses.setAttribute("user", u);
                    responseObject.addProperty("message", "Client");
                }

                responseObject.addProperty("status", true);

            }
            s.close();
        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
