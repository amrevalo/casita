/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.service;

import com.google.gson.Gson;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import net.daw.beans.LineadepedidoBean;
import net.daw.beans.ReplyBean;
import net.daw.connection.ConnectionInterface;
import net.daw.dao.LineadepedidoDao;
import net.daw.helper.AppConfigurationHelper;
import static net.daw.helper.AppConfigurationHelper.getJsonMsgDepth;
import net.daw.helper.FilterBeanHelper;
import net.daw.helper.Log4j;
import net.daw.helper.ParameterCook;

/**
 *
 * @author alumno
 */
public class LineadepedidoService implements EmptyServiceInterface, ViewServiceInterface, TableServiceInterface {
    
    HttpServletRequest oRequest = null;

    public LineadepedidoService(HttpServletRequest request) {
        oRequest = request;
    }

    private Boolean checkPermission(String strMethodName) throws Exception {
        LineadepedidoBean oUsuarioBean = (LineadepedidoBean) oRequest.getSession().getAttribute("user");
        if (oUsuarioBean != null) {
            return true;
        } else {
            return false;
        }
    }

    /*
    * http://127.0.0.1:8081/conexion/json?ob=pedido&op=get&id=1
     */
    @Override
    public ReplyBean get() throws Exception {
                

        int id = Integer.parseInt(oRequest.getParameter("id"));
        Connection oConnection = null;
//        JdbcImpl oJdbc = null;
        ReplyBean oReplyBean = null;
        try {
//            oJdbc = new JdbcImpl();
//            oConnection = oJdbc.newConnection();
            oConnection = AppConfigurationHelper.getSourceConnection().newConnection();
            LineadepedidoBean oBean = new LineadepedidoBean(id);           
            LineadepedidoDao oDao = new LineadepedidoDao(oConnection);
            oBean = oDao.get(oBean, getJsonMsgDepth());
            Gson oGson = new Gson();
            String strJson = oGson.toJson(oBean);
            oReplyBean = new ReplyBean(200, strJson);
            
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (AppConfigurationHelper.getSourceConnection() != null) {
                AppConfigurationHelper.getSourceConnection().disposeConnection();
            }
            
        }
        return oReplyBean;
    }

    //http://127.0.0.1:8081/conexion/json?ob=usuario&op=getpage&np=1&rpp=1
    @Override
    public ReplyBean getpage() throws Exception {
        int np = Integer.parseInt(oRequest.getParameter("np"));
        int rpp = Integer.parseInt(oRequest.getParameter("rpp"));
        String strOrder = oRequest.getParameter("order");
        String strFilter = oRequest.getParameter("filter");
        LinkedHashMap<String, String> hmOrder = ParameterCook.getOrderParams(strOrder);
        ArrayList<FilterBeanHelper> alFilter = ParameterCook.getFilterParams(strFilter);
        Connection oConnection = null;
//        JdbcImpl oJdbc = null;
        ReplyBean oReplyBean = null;
        ArrayList<LineadepedidoBean> aloBean = null;
        try {
//            oJdbc = new JdbcImpl();
//            oConnection = oJdbc.newConnection();
            oConnection = AppConfigurationHelper.getSourceConnection().newConnection();
            LineadepedidoDao oDao = new LineadepedidoDao(oConnection);
            aloBean = oDao.getPage(rpp, np, hmOrder,alFilter);
            Gson oGson = new Gson();
            String strJson = oGson.toJson(aloBean);
            oReplyBean = new ReplyBean(200, strJson);
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (AppConfigurationHelper.getSourceConnection() != null) {
                AppConfigurationHelper.getSourceConnection().disposeConnection();
            }
        }
        return oReplyBean;
    }

    // http://127.0.0.1:8081/conexion/json?ob=usuario&op=getcount
    @Override
    public ReplyBean getcount() throws Exception {
        Long lResult;
        Connection oConnection = null;
//        JdbcImpl oJdbc = null;
        ReplyBean oReplyBean = null;
        String strFilter = oRequest.getParameter("filter");
        ArrayList<FilterBeanHelper> alFilter = ParameterCook.getFilterParams(strFilter);
        try {
//            oJdbc = new JdbcImpl();
//            oConnection = oJdbc.newConnection();
            oConnection = AppConfigurationHelper.getSourceConnection().newConnection();
            LineadepedidoDao oDao = new LineadepedidoDao(oConnection);
            lResult = oDao.getCount(alFilter);
            Gson oGson = new Gson();
            String strJson = oGson.toJson(lResult);
            oReplyBean = new ReplyBean(200, strJson);
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (AppConfigurationHelper.getSourceConnection() != null) {
                AppConfigurationHelper.getSourceConnection().disposeConnection();
            }
        }
        return oReplyBean;
    }

    /*
    
     */
    @Override
    public ReplyBean set() throws Exception {
        String jason = oRequest.getParameter("jason");
        Connection oConnection = null;
 //       JdbcImpl oJdbc = null;
        ReplyBean oReplyBean = null;
        LineadepedidoBean oBean = new LineadepedidoBean();
        Gson oGson = new Gson();
        oBean = oGson.fromJson(jason, oBean.getClass());
        int iResult = 0;
        try {
//            oJdbc = new JdbcImpl();
//            oConnection = oJdbc.newConnection();
            oConnection = AppConfigurationHelper.getSourceConnection().newConnection();
            LineadepedidoDao oDao = new LineadepedidoDao(oConnection);
            iResult = oDao.set(oBean);
            String strJson = oGson.toJson(iResult);
            oReplyBean = new ReplyBean(200, strJson);
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (AppConfigurationHelper.getSourceConnection() != null) {
                AppConfigurationHelper.getSourceConnection().disposeConnection();
            }
        }
        return oReplyBean;
    }

    @Override
    public ReplyBean remove() throws Exception {
        int id = Integer.parseInt(oRequest.getParameter("id"));
        Boolean iResult = false;
        Connection oConnection = null;
//        JdbcImpl oJdbc = null;
        ReplyBean oReplyBean = null;
        try {
//            oJdbc = new JdbcImpl();
//            oConnection = oJdbc.newConnection();
            oConnection = AppConfigurationHelper.getSourceConnection().newConnection();
            LineadepedidoDao oDao = new LineadepedidoDao(oConnection);
            iResult = oDao.remove(id);
            Gson oGson = new Gson();
            String strJson = oGson.toJson(iResult);
            oReplyBean = new ReplyBean(200, strJson);
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (AppConfigurationHelper.getSourceConnection() != null) {
                AppConfigurationHelper.getSourceConnection().disposeConnection();
            }
        }
        return oReplyBean;
    }
    
    public ReplyBean getpagexpedido() throws Exception {
        //if (this.checkPermission("getpage")) {
            int np = Integer.parseInt(oRequest.getParameter("np"));
            int rpp = Integer.parseInt(oRequest.getParameter("rpp"));
            int id = Integer.parseInt(oRequest.getParameter("id"));
            String strOrder = oRequest.getParameter("order");
            String strFilter = oRequest.getParameter("filter");
            LinkedHashMap<String, String> hmOrder = ParameterCook.getOrderParams(strOrder);
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.getFilterParams(strFilter);
            Connection oConnection = null;
            ConnectionInterface oPooledConnection = null;
            ReplyBean oReplyBean = null;
            ArrayList<LineadepedidoBean> aloBean = null;
            try {
                oPooledConnection = AppConfigurationHelper.getSourceConnection();
                oConnection = oPooledConnection.newConnection();
                LineadepedidoDao oDao = new LineadepedidoDao(oConnection);
                aloBean = oDao.getPagexpedido(rpp, np, hmOrder, alFilter, id);
                Gson oGson = AppConfigurationHelper.getGson();
                String strJson = oGson.toJson(aloBean);
                oReplyBean = new ReplyBean(200, strJson);
            } catch (Exception ex) {
                String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
                Log4j.errorLog(msg, ex);
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oPooledConnection != null) {
                    oPooledConnection.disposeConnection();
                }
            }
            return oReplyBean;
   //     } else {
   //         return new ReplyBean(401, "Unauthorized");
   //     }
    }

    /*
    * http://127.0.0.1:8081/carrito-server/json?ob=lineadepedido&op=getcountxpedido&id=1
     */
    public ReplyBean getcountxpedido() throws Exception {
 //       if (this.checkPermission("getcount")) {
            Long lResult;
            Connection oConnection = null;
            ConnectionInterface oPooledConnection = null;
            ReplyBean oReplyBean = null;
            String strFilter = oRequest.getParameter("filter");
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.getFilterParams(strFilter);
            int id = Integer.parseInt(oRequest.getParameter("id"));
            try {
                oPooledConnection = AppConfigurationHelper.getSourceConnection();
                oConnection = oPooledConnection.newConnection();
                LineadepedidoDao oDao = new LineadepedidoDao(oConnection);
                lResult = oDao.getCountxpedido(alFilter, id);
                Gson oGson = AppConfigurationHelper.getGson();
                String strJson = oGson.toJson(lResult);
                oReplyBean = new ReplyBean(200, strJson);
            } catch (Exception ex) {
                String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
                Log4j.errorLog(msg, ex);
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oPooledConnection != null) {
                    oPooledConnection.disposeConnection();
                }
            }
            return oReplyBean;
//        } else {
//            return new ReplyBean(401, "Unauthorized");
        }
    
    
    public ReplyBean getpagexproducto() throws Exception {
        //if (this.checkPermission("getpage")) {
            int np = Integer.parseInt(oRequest.getParameter("np"));
            int rpp = Integer.parseInt(oRequest.getParameter("rpp"));
            int id = Integer.parseInt(oRequest.getParameter("id"));
            String strOrder = oRequest.getParameter("order");
            String strFilter = oRequest.getParameter("filter");
            LinkedHashMap<String, String> hmOrder = ParameterCook.getOrderParams(strOrder);
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.getFilterParams(strFilter);
            Connection oConnection = null;
            ConnectionInterface oPooledConnection = null;
            ReplyBean oReplyBean = null;
            ArrayList<LineadepedidoBean> aloBean = null;
            try {
                oPooledConnection = AppConfigurationHelper.getSourceConnection();
                oConnection = oPooledConnection.newConnection();
                LineadepedidoDao oDao = new LineadepedidoDao(oConnection);
                aloBean = oDao.getPagexproducto(rpp, np, hmOrder, alFilter, id);
                Gson oGson = AppConfigurationHelper.getGson();
                String strJson = oGson.toJson(aloBean);
                oReplyBean = new ReplyBean(200, strJson);
            } catch (Exception ex) {
                String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
                Log4j.errorLog(msg, ex);
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oPooledConnection != null) {
                    oPooledConnection.disposeConnection();
                }
            }
            return oReplyBean;
   //     } else {
   //         return new ReplyBean(401, "Unauthorized");
   //     }
    }

    /*
    * http://127.0.0.1:8081/carrito-server/json?ob=lineadepedido&op=getcountxpedido&id=1
     */
    public ReplyBean getcountxproducto() throws Exception {
 //       if (this.checkPermission("getcount")) {
            Long lResult;
            Connection oConnection = null;
            ConnectionInterface oPooledConnection = null;
            ReplyBean oReplyBean = null;
            String strFilter = oRequest.getParameter("filter");
            ArrayList<FilterBeanHelper> alFilter = ParameterCook.getFilterParams(strFilter);
            int id = Integer.parseInt(oRequest.getParameter("id"));
            try {
                oPooledConnection = AppConfigurationHelper.getSourceConnection();
                oConnection = oPooledConnection.newConnection();
                LineadepedidoDao oDao = new LineadepedidoDao(oConnection);
                lResult = oDao.getCountxproducto(alFilter, id);
                Gson oGson = AppConfigurationHelper.getGson();
                String strJson = oGson.toJson(lResult);
                oReplyBean = new ReplyBean(200, strJson);
            } catch (Exception ex) {
                String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
                Log4j.errorLog(msg, ex);
                throw new Exception(msg, ex);
            } finally {
                if (oConnection != null) {
                    oConnection.close();
                }
                if (oPooledConnection != null) {
                    oPooledConnection.disposeConnection();
                }
            }
            return oReplyBean;
//        } else {
//            return new ReplyBean(401, "Unauthorized");
        }
}
