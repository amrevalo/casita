/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.daw.beans.ReplyBean;
import net.daw.connection.ConnectionInterface;
import net.daw.helper.AppConfigurationHelper;
import net.daw.helper.EstadoHelper;
import net.daw.helper.EstadoHelper.Tipo_estado;
import net.daw.helper.Log4j;
import net.daw.helper.MappingHelper;
import static net.daw.helper.ParameterCook.prepareCamelCaseObject;
import net.daw.service.ViewServiceInterface;


/**
 *
 * @author alumno
 */
public class json extends HttpServlet {

    private void retardo(Integer iLast) {
        try {
            Thread.sleep(iLast);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, Exception {
        //response.setContentType("application/json;charset=UTF-8");
        ReplyBean oReplyBean = null;
        try (PrintWriter out = response.getWriter()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (Exception ex) {
                oReplyBean = new ReplyBean(500, "Database Connection Error: Please contact your administrator");
            }
            retardo(0);
            String ob = prepareCamelCaseObject(request);
            String op = request.getParameter("op");
            if (("".equalsIgnoreCase(ob) && "".equalsIgnoreCase(op)) || (ob == null && op == null)) {
                Connection oConnection = null;
                ConnectionInterface oPooledConnection = null;
                try {
                    oPooledConnection = AppConfigurationHelper.getSourceConnection();
                    oConnection = oPooledConnection.newConnection();
                    response.setContentType("text/html;charset=UTF-8");
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Carrito server</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.print("<h1>Bienvenidos a carrito-server</h1>");
                    out.println("<h2>Servlet json at " + request.getContextPath() + "</h2>");
                    out.print("<h3>Conexión OK</h3>");
                    out.print("<h1>Bienvenidos a Proyecto Servidor</h1>");
                    out.println("<h2>Servlet json at " + request.getContextPath() + "</h2>");
                    out.print("<h3>Conexión OK</h3>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=usuario&op=login&user=alberto&pass=1234'>Login</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=usuario&op=check'>Check usuario</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=producto&op=getpage&np=1&rpp=3'>Productos Paginados</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=pedido&op=getpage&np=1&rpp=10'>Todos Pedidos</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=carrito&op=add&id=1&cantidad=5'>Add Carrito prod 1 cant 5</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=carrito&op=add&id=2&cantidad=6'>Add Carrito prod 2 cant 6</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=carrito&op=list'>check carrito</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=carrito&op=buy'>Comprar</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=carrito&op=list'>Listar Carrito</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=carrito&op=empty'>Vaciar Carrito</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=pedido&op=get&id=11'>Pedido Creado</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=lineadepedido&op=getpagexpedido&id=11&np=1&rpp=10'>Linea de Pedido Creado</a></br>");
                    out.print("<a href='http://localhost:8081/carrito-server/json?ob=usuario&op=logout'>Desconectar</a></br>");                        
                    out.println("</body>");
                    out.println("</html>");
                } catch (Exception ex) {
                    response.setContentType("text/html;charset=UTF-8");
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Carrito server</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.print("<h1>Bienvenidos a carrito-server</h1>");
                    out.println("<h2>Servlet json at " + request.getContextPath() + "</h2>");
                    out.print("<h3>Conexión KO</h3>");
                    out.println("</body>");
                    out.println("</html>");
                } finally {
                    if (oConnection != null) {
                        oConnection.close();
                    }
                    if (oPooledConnection != null) {
                        oPooledConnection.disposeConnection();
                    }
                }
            } else {
                response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
                //response.setHeader("Access-Control-Allow-Methods", "PATCH, POST, GET, PUT, OPTIONS, DELETE");
                response.setHeader("Access-Control-Allow-Methods", "GET,POST");
                response.setHeader("Access-Control-Max-Age", "86400");
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, x-requested-with, Content-Type");
                try {
                      oReplyBean = (ReplyBean) MappingHelper.executeMethodService(request);
//                    String strClassName = "net.daw.service." + ob + "Service";
//                    ViewServiceInterface oService = (ViewServiceInterface) Class.forName(strClassName).getDeclaredConstructor(HttpServletRequest.class).newInstance(request);
//                    Method oMethodService = oService.getClass().getMethod(op);
//                    oReplyBean = (ReplyBean) oMethodService.invoke(oService);
                } catch (Exception ex) {
                    if (EstadoHelper.getTipo_estado() == Tipo_estado.Debug) {
                        out.println(ex);
                        ex.printStackTrace(out);
                    } else {
                        oReplyBean = new ReplyBean(500, "carrito-server error. Please, contact your administrator.");
                    }
                    Log4j.errorLog(this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName(), ex);
                    oReplyBean = new ReplyBean(500, "Object or Operation not found : Please contact your administrator");
                }
                out.print("{\"status\":" + oReplyBean.getCode() + ", \"json\":" + oReplyBean.getJson() + "}");
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
        } catch (Exception ex) {
            Logger.getLogger(json.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(json.class.getName()).log(Level.SEVERE, null, ex);
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
