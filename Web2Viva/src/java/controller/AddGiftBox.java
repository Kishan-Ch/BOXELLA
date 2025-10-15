/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.GiftBox;
import hibernate.HibernateUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author ASUS
 */
@MultipartConfig
@WebServlet(name = "AddGiftBox", urlPatterns = {"/AddGiftBox"})
public class AddGiftBox extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String boxName = request.getParameter("boxName");
        String boxDesc = request.getParameter("boxDesc");
        String boxprice = request.getParameter("boxprice");
        String boxQty = request.getParameter("boxQty");

        System.out.println(boxName);
        System.out.println(boxDesc);
        System.out.println(boxprice);
        System.out.println();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        if (boxName.isEmpty()) {
            responseObject.addProperty("message", "Please Enter your Box Name first!");
        } else if (boxDesc.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Description!");
        } else if (boxprice.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Price!");
        } else if (!Util.isDouble(boxprice)) {
            responseObject.addProperty("message", "Invaid Price!");
        } else if (boxQty.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Price!");
        } else if (!Util.isInteger(boxQty)) {
            responseObject.addProperty("message", "Invaid Quantity!");
        } else {

            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            GiftBox giftBox1 = new GiftBox();
            giftBox1.setName(boxName);
            giftBox1.setDescription(boxDesc);
            giftBox1.setPrice(Double.parseDouble(boxprice));
            giftBox1.setQty(Integer.parseInt(boxQty));

            int id = (int) s.save(giftBox1);
            s.beginTransaction().commit();

            Part part1 = request.getPart("Boximg1");
            Part part2 = request.getPart("Boximg2");
            Part part3 = request.getPart("Boximg3");
            Part part4 = request.getPart("Boximg4");

            String appPath = getServletContext().getRealPath("");

            String newPath = appPath.replace("build" + File.separator + "web", "web" + File.separator + "assets" + File.separator + "product-images" + File.separator + "giftBox-images");

            File productFolder = new File(newPath, String.valueOf(id));
            productFolder.mkdir();

            File file1 = new File(productFolder, "image1.jpeg");
            Files.copy(part1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

            File file2 = new File(productFolder, "image2.jpeg");
            Files.copy(part2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

            File file3 = new File(productFolder, "image3.jpeg");
            Files.copy(part3.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

            File file4 = new File(productFolder, "image4.jpeg");
            Files.copy(part4.getInputStream(), file4.toPath(), StandardCopyOption.REPLACE_EXISTING);

            responseObject.addProperty("status", true);
            responseObject.addProperty("message", "Gift Box Added Succefully");

            s.close();
        }

        Gson gson = new Gson();
        String responseText = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
