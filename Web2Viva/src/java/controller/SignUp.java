package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.UserType;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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

@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject user = gson.fromJson(request.getReader(), JsonObject.class);

        String firstName = user.get("firstName").getAsString();
        String lastName = user.get("lastName").getAsString();
        String email = user.get("email").getAsString();
        String password = user.get("password").getAsString();
        String confirmPassword = user.get("confirmPassword").getAsString();

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
        } else if (password.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your Password!");
        } else if (!Util.isPasswordValid(password)) {
            responseObject.addProperty("message", "The Password must contains at least uppercase, lowercase, number, special character and to be minimum 8 characters long!");
        } else if (confirmPassword.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Confirm Password!");
        } else if (!password.equals(confirmPassword)) {
            responseObject.addProperty("message", "The Passwords Fields are not same!");
        } else {
            
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("email", email));

            if (!c.list().isEmpty()) {
                responseObject.addProperty("message", "User already exists!");
            } else {
                
                UserType userType = (UserType) s.get(UserType.class, 2);

                User u = new User();
                u.setFirstName(firstName);
                u.setLastName(lastName);
                u.setEmail(email);
                u.setPassword(password);
                u.setUserType(userType);
                u.setVerification("verified");

                u.setCreated_at(new Date());

                s.save(u);
                s.beginTransaction().commit();

                //session management
                HttpSession ses = request.getSession();
                ses.setAttribute("email", email);

                responseObject.addProperty("status", true);
                responseObject.addProperty("message", "Regsitration Successfully. Sign In Now");

            }
            
            s.close();

        }

        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
