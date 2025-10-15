/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Category;
import hibernate.HibernateUtil;
import hibernate.Item;
import hibernate.Status;
import hibernate.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import javax.servlet.http.Part;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ASUS
 */
@MultipartConfig
@WebServlet(name = "SaveProduct", urlPatterns = {"/SaveProduct"})
public class SaveProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String prName = request.getParameter("prName");
        String prDesc = request.getParameter("prDesc");
        String categoryId = request.getParameter("category");
        String price = request.getParameter("price");
        String qty = request.getParameter("qty");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        Session s = HibernateUtil.getSessionFactory().openSession();

        Transaction tr = s.beginTransaction();

        if (prName.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Product Name First!");
        } else if (prDesc.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Description!");
        } else if (prDesc.isEmpty()) {
            responseObject.addProperty("message", "Please Enter Description!");
        } else {
        }

        User user = (User) request.getSession().getAttribute("adminUser");
        try {

            if (user != null) {
                Criteria c1 = s.createCriteria(Category.class);
                c1.add(Restrictions.eq("id", Integer.parseInt(categoryId)));

                if (!c1.list().isEmpty()) {
                    Category category = (Category) c1.uniqueResult();
                    Status status = (Status) s.get(Status.class, 1);

                    Item item = new Item();
                    item.setName(prName);
                    item.setDescription(prDesc);
                    item.setCategory(category);
                    item.setSize(0);
                    item.setPrice(Double.parseDouble(price));
                    item.setQty(Integer.parseInt(qty));
                    item.setUser(user);
                    item.setCreated_at(new Date());
                    item.setStatus(status);

                    int id = (int)s.save(item);
                    tr.commit();

                    Part part1 = request.getPart("img1");
                    Part part2 = request.getPart("img2");
                    Part part3 = request.getPart("img3");
                    Part part4 = request.getPart("img4");

                    String appPath = getServletContext().getRealPath("");

                    String newPath = appPath.replace("build" + File.separator + "web", "web" + File.separator + "assets" + File.separator + "product-images");

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
                    responseObject.addProperty("message", "Product Added Succefully");

                } else {
                    responseObject.addProperty("message", "Invalid Category");
                }

            } else {
                responseObject.addProperty("message", "Session Expired. Sign in First!");
            }
        } catch (Exception e) {

            if (tr != null && tr.isActive()) {
                tr.rollback();
            }
        } finally {
            Gson gson = new Gson();
            String toJson = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(toJson);
            s.close();
        }

    }

}
