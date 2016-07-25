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
@WebServlet(name = "LoadSeller", urlPatterns = {"/LoadSeller"})
public class LoadServer extends HttpServlet {

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
            String type = request.getParameter("server_type");
            String seller_name = request.getParameter("seller_name");
            if(seller_name != null){
                seller_name = seller_name.trim();
            }
            String rate = request.getParameter("userrate");
            String url = "jdbc:mysql://localhost/housemap?useUnicode=true&characterEncoding=utf8";
            String name = "com.mysql.jdbc.Driver";
            String username = "root";
            String password = "";
            Connection conn = null;
            PreparedStatement pst;
            ArrayList<String> seller = new ArrayList<>();
            if ("LOAD_SELLER".equals(type)) {
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select name,detail,img,coordx,coordy from seller";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    seller.add(rs.getString(1) + "_" + rs.getString(2) + "_" + rs.getString(3) + "_" + rs.getString(4) + "_" + rs.getString(5));
                }
                out.println(seller);
            }else if("LOAD_DETAIL".equals(type)){
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select price,detail,img,location,rate from seller where name = '"+ seller_name + "';";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();
                rs.next();
                String price_ = rs.getString(1);
                String detail_ = rs.getString(2);
                String img_ = rs.getString(3);
                String location_ = rs.getString(4);
                String rate_ = rs.getString(5);
                String jsonstr = "{\"price\":\"" + price_ + "\",\"detail\":\"" + detail_ + "\",\"img\":\"" + img_ + "\",\"location\":\"" + location_ + "\",\"rate\":" + rate_ + "}";
                out.println(jsonstr);
            }else if("UPDATERATE".equals(type)){
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select rate from seller where name = '"+ seller_name + "';";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();
                rs.next();
                float old_rate = Float.valueOf(rs.getString(1));
                float update_rate = Float.valueOf(rate);
                float new_rate = (old_rate + update_rate)/2;
                pst.close();
                rs.close();
                sql = "update seller set rate = '"+ new_rate + "' where name = '" + seller_name + "';";
                pst = conn.prepareStatement(sql);//准备执行语句  
                pst.executeUpdate();
                boolean success = true;
                String jsonstr = "{\"success\":" + success + "}";
                out.println(jsonstr);
            }else if("GET_COORD".equals(type)){
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select coordx,coordy from seller where name = '"+ seller_name + "';";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();
                rs.next();
                String coordx = rs.getString(1);
                String coordy = rs.getString(2);
                String jsonstr = "{\"coordx\":\""+ coordx +"\",\"coordy\":\""+ coordy + "\"}";
                out.println(jsonstr);
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
            Logger.getLogger(LoadServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoadServer.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(LoadServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoadServer.class.getName()).log(Level.SEVERE, null, ex);
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
