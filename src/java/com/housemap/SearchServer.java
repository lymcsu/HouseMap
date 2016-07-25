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
@WebServlet(name = "SearchServer", urlPatterns = {"/SearchServer"})
public class SearchServer extends HttpServlet {

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
            String keyword = request.getParameter("keyword");
            String location = request.getParameter("location");
            String url = "jdbc:mysql://localhost/housemap?useUnicode=true&characterEncoding=utf8";
            String name = "com.mysql.jdbc.Driver";
            String username = "root";
            String password = "";
            Connection conn = null;
            PreparedStatement pst;
            if (keyword != "") {
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select name,coordx,coordy,detail,img from seller where (name like " + "\"%" + keyword + "%\" or detail like " + "\"%" + keyword + "%\") and type like \"%" + type + "%\" and district like \"%" + location +"%\"";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();
                String str = "";
                while (rs.next()) {
                    str += "{\"name\":\"" + rs.getString(1) + "\",\"coordx\":" + rs.getString(2) + ",\"coordy\":" + rs.getString(3) + ",\"detail\":\"" + rs.getString(4) + "\",\"img\":\"" + rs.getString(5) + "\"}_";
                }
                out.print(str);
            } else {
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select name,coordx,coordy,detail,img from seller where type like \"%" + type + "%\" and district like \"%" + location +"%\"";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();
                String str = "";
                while (rs.next()) {
                    str += "{\"name\":\"" + rs.getString(1) + "\",\"coordx\":" + rs.getString(2) + ",\"coordy\":" + rs.getString(3) + ",\"detail\":\"" + rs.getString(4) + "\",\"img\":\"" + rs.getString(5) + "\"}_";
                }
                out.print(str);
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
            Logger.getLogger(SearchServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SearchServer.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(SearchServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SearchServer.class.getName()).log(Level.SEVERE, null, ex);
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
