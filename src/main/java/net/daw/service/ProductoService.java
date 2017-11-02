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
import net.daw.beans.ProductoBean;
import net.daw.beans.ReplyBean;
import net.daw.connection.JdbcImpl;
import net.daw.dao.ProductoDao;
import net.daw.helper.AppConfigurationHelper;
import static net.daw.helper.AppConfigurationHelper.getJsonMsgDepth;
import net.daw.helper.FilterBeanHelper;
import net.daw.helper.ParameterCook;

/**
 *
 * @author alumno
 */
public class ProductoService implements EmptyServiceInterface, ViewServiceInterface, TableServiceInterface{
    
     HttpServletRequest oRequest = null;

    public ProductoService(HttpServletRequest request) {
        oRequest = request;
    }

    private Boolean checkPermission(String strMethodName) throws Exception {
        ProductoBean oUsuarioBean = (ProductoBean) oRequest.getSession().getAttribute("user");
        if (oUsuarioBean != null) {
            return true;
        } else {
            return false;
        }
    }

    /*
    * http://127.0.0.1:8081/conexion/json?ob=usuario&op=get&id=1
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
            ProductoBean oBean = new ProductoBean(id);           
            ProductoDao oDao = new ProductoDao(oConnection);
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
        ArrayList<ProductoBean> aloBean = null;
        try {
//            oJdbc = new JdbcImpl();
//            oConnection = oJdbc.newConnection();
            oConnection = AppConfigurationHelper.getSourceConnection().newConnection();
            ProductoDao oDao = new ProductoDao(oConnection);
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
            ProductoDao oDao = new ProductoDao(oConnection);
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
//        JdbcImpl oJdbc = null;
        ReplyBean oReplyBean = null;
        ProductoBean oBean = new ProductoBean();
        Gson oGson = new Gson();
        oBean = oGson.fromJson(jason, oBean.getClass());
        int iResult = 0;
        try {
//            oJdbc = new JdbcImpl();
//            oConnection = oJdbc.newConnection();
            oConnection = AppConfigurationHelper.getSourceConnection().newConnection();
            ProductoDao oDao = new ProductoDao(oConnection);
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
            ProductoDao oDao = new ProductoDao(oConnection);
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

//    public ReplyBean login() throws Exception {
//        Connection oConnection = null;
//        JdbcImpl oJdbc = null;
//        ReplyBean oReplyBean = null;
//        UsuarioBean oUsuarioBean = new UsuarioBean();
//        oUsuarioBean.setLogin(oRequest.getParameter("user"));
//        oUsuarioBean.setPass(oRequest.getParameter("pass"));
//        if (!oUsuarioBean.getLogin().equalsIgnoreCase("") || !oUsuarioBean.getPass().equalsIgnoreCase("")) {
//            try {
//                oJdbc = new JdbcImpl();
//                oConnection = oJdbc.newConnection();
//                UsuarioDao oDao = new UsuarioDao(oConnection);
//                oUsuarioBean = oDao.getFromLoginAndPass(oUsuarioBean);
//                HttpSession oSession = oRequest.getSession();
//                oSession.setAttribute("user", oUsuarioBean);
//                Gson oGson = AppConfigurationHelper.getGson();
//                String strJson = oGson.toJson(oUsuarioBean);
//                oReplyBean = new ReplyBean(200, strJson);
//            } catch (Exception ex) {
//                throw new Exception();
//            } finally {
//                if (oConnection != null) {
//                    oConnection.close();
//                }
//                if (oJdbc != null) {
//                    oJdbc.disposeConnection();
//                }
//            }
//        }
//        return oReplyBean;
//    }
//
//    public ReplyBean logout() throws Exception {
//        HttpSession oSession = oRequest.getSession();
//        oSession.invalidate();
//        ReplyBean oReplyBean = new ReplyBean(200, "Session is closed");
//        return oReplyBean;
//    }
// 
//    public ReplyBean check() throws Exception {
//        ReplyBean oReplyBean = null;
//        UsuarioBean oUsuarioBean = null;
//        try {
//            HttpSession oSession = oRequest.getSession();
//            oUsuarioBean = (UsuarioBean) oSession.getAttribute("user");
//            Gson oGson = AppConfigurationHelper.getGson();
//            String strJson = oGson.toJson(oUsuarioBean);
//            oReplyBean = new ReplyBean(200, strJson);
//        } catch (Exception ex) {
//            throw new Exception();
//        }
// 
//        return oReplyBean;
//    }
}
