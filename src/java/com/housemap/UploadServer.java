/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.housemap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "UploadServer", urlPatterns = {"/UploadServer"})
public class UploadServer extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String image = request.getParameter("image");
            String header = "data:image/jpeg;base64,";
            String sellername = request.getParameter("name");
            String detail = request.getParameter("detail");
            String type = request.getParameter("type");
            String location = request.getParameter("location");
            String price = request.getParameter("price");
            String coordx = request.getParameter("coordx");
            String coordy = request.getParameter("coordy");
            String district = request.getParameter("district");
            String user_name = request.getParameter("username");
            String url = "jdbc:mysql://localhost/housemap?useUnicode=true&characterEncoding=utf8";
            String name = "com.mysql.jdbc.Driver";
            String username = "root";
            String password = "";
            Connection conn = null;
            PreparedStatement pst;
            if (image.indexOf(header) != 0) {
                out.print("{\"success\":false}");
                return;
            }
            image = image.substring(header.length());
            boolean success = false;
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                byte[] decodedBytes = decoder.decodeBuffer(image);
                String imgFilePath = "E:\\webproject\\project\\HouseMap\\web\\images\\sellerimg\\" + sellername + ".jpg";
                FileOutputStream output = new FileOutputStream(imgFilePath);
                output.write(decodedBytes);
                output.close();
                success = true;
            } catch (Exception e) {
                success = false;
                e.printStackTrace();
            }
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url, username, password);//获取连接 
            String sql = "insert into seller(name,price,location,detail,type,img,coordx,coordy,user,rate,district)values('"+ sellername + "','"+ price +"','"+ location +"','"+ detail + "','"+ type +"','"+ sellername +".jpg','" + coordx + "','" + coordy + "','"+ user_name +"','"+ 3 +"','"+ district + "')";
            pst = conn.prepareStatement(sql);//准备执行语句  
            pst.executeUpdate();
            out.println("{\"success\":" + success + "}");
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UploadServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UploadServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UploadServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(UploadServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
