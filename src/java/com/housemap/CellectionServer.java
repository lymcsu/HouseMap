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
@WebServlet(name = "CellectionServer", urlPatterns = {"/CellectionServer"})
public class CellectionServer extends HttpServlet {

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
            String user_name = request.getParameter("username");
            String seller_name = request.getParameter("sellername");
            if(seller_name != null){
                seller_name = seller_name.trim();
            }
            String url = "jdbc:mysql://localhost/housemap?useUnicode=true&characterEncoding=utf8";
            String name = "com.mysql.jdbc.Driver";
            String username = "root";
            String password = "";
            Connection conn = null;
            PreparedStatement pst;
            boolean isclc = false;
            ArrayList<String> myclcs = new ArrayList<>();
            if ("ADD_CLC".equals(type)) {
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "insert into cellection(username,sellername)values('" + user_name + "','" + seller_name + "')";
                pst = conn.prepareStatement(sql);//准备执行语句  
                pst.executeUpdate();
                out.println("收藏成功!");
            } else if ("CANCEL_CLC".equals(type)) {
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "delete from cellection where username = '" + user_name + "' and sellername = '" + seller_name + "';";
                pst = conn.prepareStatement(sql);//准备执行语句  
                int num = pst.executeUpdate();
                if(num == 1){
                    out.println("取消收藏!");
                }else{
                    out.println("取消失败!");
                }
            } else if ("IS_CLC".equals(type)) {
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select count(1) from cellection where username = '" + user_name + "' and sellername = '" + seller_name + "';";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();
                rs.next();
                if (rs.getInt(1) == 1) {
                    //显示收藏
                    isclc = true;
                    out.print(isclc);
                }else{
                    out.print(isclc);
                }
            }else if("LOAD_CLC".equals(type)){
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select sellername from cellection where username = '" + user_name + "';";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    myclcs.add(rs.getString(1));
                }
                out.print(myclcs);
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
            Logger.getLogger(CellectionServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CellectionServer.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CellectionServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CellectionServer.class.getName()).log(Level.SEVERE, null, ex);
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
