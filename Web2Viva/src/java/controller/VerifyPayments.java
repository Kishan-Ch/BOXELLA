/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.PayHere;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "VerifyPayments", urlPatterns = {"/VerifyPayments"})
public class VerifyPayments extends HttpServlet {

    private static final int PAYHERE_SUCCESS = 2;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String merchant_id = request.getParameter("merchant_id");
        String order_id = request.getParameter("order_id");
        String payHere_amount = request.getParameter("payHere_amount");
        String payHere_currency = request.getParameter("payHere_currency");
        String status_code = request.getParameter("status_code");
        String md5sig = request.getParameter("md5sig");

        String merchantSecret = "MzczMjgwNzc1NDE5NzM3NzU2NjQwMTk2MzEzOTMyMDkxMDQxNTI2";
        String merchantSecretMD5 = PayHere.generateMD5(merchantSecret);
        String hash = PayHere.generateMD5(merchant_id + order_id + payHere_amount + payHere_currency + merchantSecretMD5);

        if (md5sig.equals(hash) && Integer.parseInt(status_code) == VerifyPayments.PAYHERE_SUCCESS) {
            System.out.println("Payment Complete. Order id:"+ order_id);
            String orderId = order_id.substring(3);
            System.out.println(orderId);
        }
        
    }

}
