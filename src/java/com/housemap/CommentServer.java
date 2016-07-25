/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.housemap;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "CommentServer", urlPatterns = {"/CommentServer"})
public class CommentServer extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String type = request.getParameter("type");
            String seller_name = request.getParameter("sellername");
            if(seller_name != null){
                seller_name = seller_name.trim();
            }
            String user_name = request.getParameter("username");
            String user_comment = request.getParameter("comment");
            String url = "jdbc:mysql://localhost/housemap?useUnicode=true&characterEncoding=utf8";
            String name = "com.mysql.jdbc.Driver";
            String username = "root";
            String password = "";
            Connection conn = null;
            PreparedStatement pst;
            ArrayList<String> comments = new ArrayList<>();
            if("UPLOAD_COMMENT".equals(type)){
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "insert into comment(username,sellername,comment)values('"+ user_name +"','"+ seller_name +"','"+ user_comment +"')";
                pst = conn.prepareStatement(sql);//准备执行语句  
                pst.executeUpdate();
                boolean success = true;
                String jsonstr = "{\"success\":" + success + "}";
                out.print(jsonstr);
            }else if("LOAD_COMMENT".equals(type)){
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select username,comment from comment where sellername = '"+ seller_name +"';";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    comments.add(rs.getString(1) + "_" + rs.getString(2));
                }
                out.print(comments);
            }
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
            Logger.getLogger(CommentServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CommentServer.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CommentServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CommentServer.class.getName()).log(Level.SEVERE, null, ex);
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
