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
@WebServlet(name = "LoginServer", urlPatterns = {"/LoginServer"})
public class LoginServer extends HttpServlet {

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
            String user = request.getParameter("username");
            String pass = request.getParameter("pass");
            String realname = request.getParameter("realname");
            String tel = request.getParameter("tel");
            String email = request.getParameter("email");
            String type = request.getParameter("type");
            String url = "jdbc:mysql://localhost/housemap?useUnicode=true&characterEncoding=utf8";
            String name = "com.mysql.jdbc.Driver";
            String username = "root";
            String password = "";
            Connection conn = null;
            PreparedStatement pst;
            boolean success = false;
            String message;

            if ("LOGIN".equals(type)) {
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select username,realname,tel,email from user where username='" + user + "' and pass=md5('" + pass + "')";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    success = true;
                    String outuser = rs.getString(1);
                    String outrealname = rs.getString(2);
                    String outtel = rs.getString(3);
                    String outemail = rs.getString(4);
                    String jsonstr = "{\"success\":" + success + ",\"username\":\"" + outuser + "\",\"email\":\"" + outemail + "\",\"realname\":\"" + outrealname + "\",\"tel\":\"" + outtel + "\"}";
                    out.println(jsonstr);
                    return;
                }
                success = false;
                String jsonstr = "{\"success\":" + success + "}";
                out.println(jsonstr);
            } else if ("REGISTER".equals(type)) {
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "select count(1) from user where username='" + user + "'";
                pst = conn.prepareStatement(sql);//准备执行语句  
                ResultSet rs = pst.executeQuery();
                rs.next();
                if (rs.getInt(1) == 1) {
                    success = false;
                    message = "该用户已存在!";
                    String jsonstr = "{\"success\":" + success + ",\"message\":\"" + message + "\"}";
                    out.println(jsonstr);
                    return;
                }
                pst.close();
                rs.close();
                sql = "insert into user(username,pass,realname,tel,email) values('" + user + "',md5('" + pass + "'),'" + realname + "','" + tel + "','" + email + "')";
                pst = conn.prepareStatement(sql);//准备执行语句  
                pst.executeUpdate();
                success = true;
                String jsonstr = "{\"success\":" + success + ",\"user\":\"" + user + "\"}";
                out.println(jsonstr);
            }else if("MODIFY".equals(type)){
                Class.forName(name);//指定连接类型  
                conn = DriverManager.getConnection(url, username, password);//获取连接 
                String sql = "update user set realname = '" + realname + "',tel = '"+ tel +"',email = '"+ email + "' where username = '" + user + "';";
                pst = conn.prepareStatement(sql);//准备执行语句  
                pst.executeUpdate();
                out.print("true");
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
            Logger.getLogger(LoginServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginServer.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(LoginServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LoginServer.class.getName()).log(Level.SEVERE, null, ex);
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
