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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "VerifyAccount", urlPatterns = {"/VerifyAccount"})
public class VerifyAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String vcode = request.getParameter("vcode");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        
        Gson gson = new Gson();

        if (vcode.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Verification code First!");
        } else {
        
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            
            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("verification", vcode));
            
            if (c.list().isEmpty()) {
                responseObject.addProperty("message", "Invalid Verification Code!");
            } else {
                
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
