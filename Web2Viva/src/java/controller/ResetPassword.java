/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "ResetPassword", urlPatterns = {"/ResetPassword"})
public class ResetPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        String newPassword = user.get("newPassword").getAsString();
        String confirmPassword = user.get("confirmPassword").getAsString();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (newPassword.isEmpty()) {
            responseObject.addProperty("message", "Please Enter New your Password!");
        } else if (!Util.isPasswordValid(newPassword)) {
            responseObject.addProperty("message", "The Password must contains at least uppercase, lowercase, number, special character and to be minimum 8 characters long!");
        } else if (confirmPassword.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your Confirm Password!");
        } else if (!newPassword.equals(confirmPassword)) {
            responseObject.addProperty("message", "The Passwords Fields are not same!");
        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            HttpSession ses = request.getSession();
            String email = ses.getAttribute("email").toString();

            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("email", email));

            if (c.list().isEmpty()) {
                responseObject.addProperty("message", "Verification code Expired. Please try again!");
            } else {

                User u = (User) c.list().get(0);
                
                u.setPassword(newPassword);
                
                s.update(u);
                s.beginTransaction().commit();

                responseObject.addProperty("status", true);

                responseObject.addProperty("message", "Verification Successfully");
            }

            s.close();

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
