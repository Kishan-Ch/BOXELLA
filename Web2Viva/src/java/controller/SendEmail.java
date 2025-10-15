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
import model.Mail;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "SendEmail", urlPatterns = {"/SendEmail"})
public class SendEmail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        final String email = request.getParameter("email");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (email.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your Registered Email Address");
        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c = s.createCriteria(User.class);
            c.add(Restrictions.eq("email", email));

            if (!c.list().isEmpty()) {

                User u = (User) c.list().get(0);

                final String verificationCode = Util.generateCode();
                u.setVerification(verificationCode);

                HttpSession ses = request.getSession();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mail.sendMail(email, "Boxella - Verification", "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n"
                                + "        <!-- LOGO -->\n"
                                + "        <tr>\n"
                                + "            <td bgcolor=\"#FFA73B\" align=\"center\">\n"
                                + "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
                                + "                    <tr>\n"
                                + "                        <td align=\"center\" valign=\"top\" style=\"padding: 40px 10px 40px 10px;\"> </td>\n"
                                + "                    </tr>\n"
                                + "                </table>\n"
                                + "            </td>\n"
                                + "        </tr>\n"
                                + "        <tr>\n"
                                + "            <td bgcolor=\"#FFA73B\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n"
                                + "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
                                + "                    <tr>\n"
                                + "                        <td bgcolor=\"#ffffff\" align=\"center\" valign=\"top\" style=\"padding: 40px 20px 20px 20px; border-radius: 4px 4px 0px 0px; color: #111111; font-family: \"Lato\", Helvetica, Arial, sans-serif; font-size: 48px; font-weight: 400; letter-spacing: 4px; line-height: 48px;\">\n"
                                + "                            <h1 style=\"font-size: 48px; font-weight: 400; font-family: \"Lato\", Helvetica, Arial, sans-serif; margin: 2;\">Reset Password!</h1> <img src=\" https://img.icons8.com/clouds/100/000000/handshake.png\" width=\"125\" height=\"120\" style=\"display: block; border: 0px;\" />\n"
                                + "                        </td>\n"
                                + "                    </tr>\n"
                                + "                </table>\n"
                                + "            </td>\n"
                                + "        </tr>\n"
                                + "        <tr>\n"
                                + "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px; \">\n"
                                + "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
                                + "                    <tr>\n"
                                + "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 20px 30px 40px 30px; color: #666666; font-family: \"Lato\", Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n"
                                + "                            <p style=\"margin: 0;  margin-top: 20px;\" align=\"center\">You are almost set to start enjoying <b>BOXELLA</b>. Simply copy the verofication code below to reset password and get started.</p>\n"
                                + "                        </td>\n"
                                + "                    </tr>\n"
                                + "                    <tr>\n"
                                + "                        <td bgcolor=\"#ffffff\" align=\"left\">\n"
                                + "                            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                                + "                                <tr>\n"
                                + "                                    <td bgcolor=\"#ffffff\" align=\"center\" style=\"padding: 20px 30px 60px 30px;\">\n"
                                + "                                        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                                + "                                            <tr>\n"
                                + "                                                <td align=\"center\"  bgcolor=\"#FFA73B\" target=\"_blank\" style=\"border-radius: 3px; font-size: 20px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; \n"
                                + "                                                color: #ffffff; text-decoration: none; padding: 15px 25px; border-radius: 2px; margin-top:20px; border: 1px solid #FFA73B; display: inline-block;\">" + verificationCode + "</td>\n"
                                + "                                            </tr>\n"
                                + "                                        </table>\n"
                                + "                                    </td>\n"
                                + "                                </tr>\n"
                                + "                            </table>\n"
                                + "                        </td>\n"
                                + "                    </tr> <!-- COPY -->\n"
                                + "                   \n"
                                + "                    <tr>\n"
                                + "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 20px 30px; color: #666666; font-family: \"Lato\", Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n"
                                + "                            <p style=\"margin: 0;\">If you have any questions, just reply to this email&mdash;we,re always happy to help out.</p>\n"
                                + "                        </td>\n"
                                + "                    </tr>\n"
                                + "                    <tr>\n"
                                + "                        <td bgcolor=\"#ffffff\" align=\"left\" style=\"padding: 0px 30px 40px 30px; border-radius: 0px 0px 4px 4px; color: #666666; font-family: \"Lato\", Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n"
                                + "                            <p style=\"margin: 0;\">Cheers,<br>Boxella Team</p>\n"
                                + "                        </td>\n"
                                + "                    </tr>\n"
                                + "                </table>\n"
                                + "            </td>\n"
                                + "        </tr>\n"
                                + "        <tr>\n"
                                + "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 30px 10px 0px 10px;\">\n"
                                + "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
                                + "                    <tr>\n"
                                + "                        <td bgcolor=\"#FFECD1\" align=\"center\" style=\"padding: 30px 30px 30px 30px; border-radius: 4px 4px 4px 4px; color: #666666; font-family: \"Lato\", Helvetica, Arial, sans-serif; font-size: 18px; font-weight: 400; line-height: 25px;\">\n"
                                + "                            <h2 style=\"font-size: 20px; font-weight: 400; color: #111111; margin: 0;\">Need more help?</h2>\n"
                                + "                            <p style=\"margin: 0;\"><a href=\"#\" target=\"_blank\" style=\"color: #FFA73B;\">We&rsquo;re here to help you out</a></p>\n"
                                + "                        </td>\n"
                                + "                    </tr>\n"
                                + "                </table>\n"
                                + "            </td>\n"
                                + "        </tr>\n"
                                + "        <tr>\n"
                                + "            <td bgcolor=\"#f4f4f4\" align=\"center\" style=\"padding: 0px 10px 0px 10px;\">\n"
                                + "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
                                + "                    <tr>\n"
                                + "                        <td bgcolor=\"#f4f4f4\" align=\"left\" style=\"padding: 0px 30px 30px 30px; color: #666666; font-family: \"Lato\", Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 18px;\"> <br>\n"
                                + "                            </td>\n"
                                + "                    </tr>\n"
                                + "                </table>\n"
                                + "            </td>\n"
                                + "        </tr>\n"
                                + "    </table>");
                    }
                }).start();

                ses.setAttribute("email", email);
                s.merge(u);
                s.beginTransaction().commit();
                responseObject.addProperty("message", "Check Your Email and Get your Verification Code!");
                responseObject.addProperty("status", true);

            } else {
                responseObject.addProperty("message", "Something went wronr! Please Register");
            }

        }

        Gson gson = new Gson();

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);

    }

}
