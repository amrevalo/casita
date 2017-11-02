/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import net.daw.beans.LineadepedidoBean;
import net.daw.beans.PedidoBean;
import net.daw.beans.ProductoBean;
import net.daw.helper.AppConfigurationHelper;
import static net.daw.helper.AppConfigurationHelper.getJsonMsgDepth;
import net.daw.helper.EncodingUtilHelper;
import net.daw.helper.FilterBeanHelper;
import net.daw.helper.Log4j;
import net.daw.helper.SqlBuilder;

/**
 *
 * @author a022593391p
 */
public class LineadepedidoDao implements DaoTableInterface<LineadepedidoBean>, DaoViewInterface<LineadepedidoBean> {
    
     private String strTable = "lineadepedido";
    private String strSQL = "select * from " + strTable + " WHERE 1=1 ";
    private Connection oConnection = null;

    public LineadepedidoDao(Connection oPooledConnection) {
        oConnection = oPooledConnection;
    }

    @Override
    public LineadepedidoBean get(LineadepedidoBean oBean, int intExpand) throws Exception {
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "select * from " + strTable + " WHERE 1=1 ";
        strSQL += " AND id=" + oBean.getId();
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                oBean.setCantidad(oResultSet.getInt("cantidad"));
                oBean.setId_producto(oResultSet.getInt("id_producto"));
                oBean.setId_pedido(oResultSet.getInt("id_pedido"));
                //pendiente la expansión ************************* %%%%%%
                if (intExpand > 0) {
                    ProductoBean oProducto = new ProductoBean();
                    ProductoDao oProductoDao = new ProductoDao(oConnection);
                    oProducto.setId(oBean.getId_producto());
                    oProducto = oProductoDao.get(oProducto, 0);
                    oBean.setObj_producto(oProducto);
                }
                if (intExpand > 0) {
                    PedidoBean oPedido = new PedidoBean();
                    PedidoDao oPedidoDao = new PedidoDao(oConnection);
                    oPedido.setId(oBean.getId_pedido());
                    oPedido = oPedidoDao.get(oPedido, --intExpand);
                    oBean.setObj_pedido(oPedido);
                }
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            //log4j 
            throw new Exception();
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }

        }
        return oBean;
    }

    @Override
    public Integer set(LineadepedidoBean oBean) throws Exception {
        PreparedStatement oPreparedStatement = null;
        Integer iResult = 0;
        Boolean insert = true;
        try {
            if (oBean.getId() == null || oBean.getId() <= 0) {
                strSQL = "INSERT INTO " + strTable;
                strSQL += "(";               
                strSQL += "cantidad,";
                strSQL += "id_pedido,";
                strSQL += "id_producto";
                strSQL += ")";
                strSQL += " VALUES ";
                strSQL += "(";
                strSQL += oBean.getCantidad() + ",";
                strSQL += oBean.getId_pedido() + ",";
                strSQL += oBean.getId_producto();
                strSQL += ")";
            } else {
                insert = false;
                strSQL = "UPDATE " + strTable;
                strSQL += " SET ";
                strSQL += "cantidad=" + oBean.getCantidad() + ",";
                strSQL += "id_pedido=" + oBean.getId_pedido() + ",";
                strSQL += "id_producto=" + oBean.getId_producto() + " ";
                strSQL += "WHERE id=" + oBean.getId();
            }
            oPreparedStatement = oConnection.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
            iResult = oPreparedStatement.executeUpdate();
            if (iResult < 1) {
                throw new Exception();
            }
            if (insert) {
                ResultSet oResultSet = oPreparedStatement.getGeneratedKeys();
                oResultSet.next();
                iResult = oResultSet.getInt(1);
            }
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iResult;
    }

    //hecho por alberto
    @Override
    public Boolean remove(Integer id) throws Exception {
        boolean iResult = false;
        strSQL = "DELETE FROM " + strTable + " WHERE id=?";
        PreparedStatement oPreparedStatement = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oPreparedStatement.setInt(1, id);
            iResult = oPreparedStatement.execute();
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iResult;
    }

    @Override
    public Long getCount(ArrayList<FilterBeanHelper> alFilter) throws Exception {
        ResultSet oResultSet = null;
        PreparedStatement oPreparedStatement = null;
        Long iResult = 0L;
        strSQL = "SELECT COUNT(*) FROM " + strTable;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            //iResult = oResultSet.getLong(1);
            if (oResultSet.next()) {
                iResult = oResultSet.getLong("COUNT(*)");
            } else {
                throw new Exception();
            }
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iResult;
    }

    @Override
    public ArrayList<LineadepedidoBean> getPage(int intRegsPerPag, int intPage, LinkedHashMap<String, String> hmOrder, ArrayList<FilterBeanHelper> alFilter) throws Exception {
        //String strSQL1 = strSQL + SqlBuilder.buildSqlLimit(this.getCount(), intRegsPerPag, intPage);
        String strSQL1 = strSQL;
        strSQL1 += SqlBuilder.buildSqlFilter(alFilter);
        strSQL1 += SqlBuilder.buildSqlOrder(hmOrder);
        strSQL1 += SqlBuilder.buildSqlLimit(this.getCount(alFilter), intRegsPerPag, intPage);
        ArrayList<LineadepedidoBean> aloBean = new ArrayList<>();
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL1);
            oResultSet = oPreparedStatement.executeQuery(strSQL1);
            while (oResultSet.next()) {
                LineadepedidoBean oBean = new LineadepedidoBean();
                oBean.setId(oResultSet.getInt("id"));
                oBean = this.get(oBean, getJsonMsgDepth());
                aloBean.add(oBean);
                //aloBean.add(this.get(new UsuarioBean(oResultSet.getInt("id"))));                
            }
        } catch (Exception ex) {
            throw new Exception();
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return aloBean;
    }
    
    
    public Long getCountxpedido(ArrayList<FilterBeanHelper> alFilter, int id_pedido) throws Exception {
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "SELECT COUNT(*) FROM " + strTable;
        strSQL += " WHERE id_pedido=" + id_pedido;
        strSQL += SqlBuilder.buildSqlFilter(alFilter);
        Long iResult = 0L;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                iResult = oResultSet.getLong("COUNT(*)");
            } else {
                String msg = this.getClass().getName() + ": getCountxpedido";
                Log4j.errorLog(msg);
                throw new Exception(msg);
            }
        } catch (Exception ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iResult;
    }
    
    
    public ArrayList<LineadepedidoBean> getPagexpedido(int intRegsPerPag, int intPage, LinkedHashMap<String, String> hmOrder, ArrayList<FilterBeanHelper> alFilter, int id_pedido) throws Exception {
        String strSQL1 = strSQL;
        strSQL1 += " and id_pedido=" + id_pedido + " ";
        strSQL1 += SqlBuilder.buildSqlFilter(alFilter);
        strSQL1 += SqlBuilder.buildSqlOrder(hmOrder);
        strSQL1 += SqlBuilder.buildSqlLimit(this.getCount(alFilter), intRegsPerPag, intPage);
        ArrayList<LineadepedidoBean> aloBean = new ArrayList<>();
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL1);
            oResultSet = oPreparedStatement.executeQuery(strSQL1);
            while (oResultSet.next()) {
                aloBean.add(this.get(new LineadepedidoBean(oResultSet.getInt("id")), AppConfigurationHelper.getJsonMsgDepth()));
            }
        } catch (Exception ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return aloBean;
    }
    
    
    
    
    public Long getCountxproducto(ArrayList<FilterBeanHelper> alFilter, int id_producto) throws Exception {
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        strSQL = "SELECT COUNT(*) FROM " + strTable;
        strSQL += " WHERE id_producto=" + id_producto;
        strSQL += SqlBuilder.buildSqlFilter(alFilter);
        Long iResult = 0L;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL);
            oResultSet = oPreparedStatement.executeQuery(strSQL);
            if (oResultSet.next()) {
                iResult = oResultSet.getLong("COUNT(*)");
            } else {
                String msg = this.getClass().getName() + ": getCountxproducto";
                Log4j.errorLog(msg);
                throw new Exception(msg);
            }
        } catch (Exception ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return iResult;
    }
    
    
    public ArrayList<LineadepedidoBean> getPagexproducto(int intRegsPerPag, int intPage, LinkedHashMap<String, String> hmOrder, ArrayList<FilterBeanHelper> alFilter, int id_producto) throws Exception {
        String strSQL1 = strSQL;
        strSQL1 += " and id_producto=" + id_producto + " ";
        strSQL1 += SqlBuilder.buildSqlFilter(alFilter);
        strSQL1 += SqlBuilder.buildSqlOrder(hmOrder);
        strSQL1 += SqlBuilder.buildSqlLimit(this.getCount(alFilter), intRegsPerPag, intPage);
        ArrayList<LineadepedidoBean> aloBean = new ArrayList<>();
        PreparedStatement oPreparedStatement = null;
        ResultSet oResultSet = null;
        try {
            oPreparedStatement = oConnection.prepareStatement(strSQL1);
            oResultSet = oPreparedStatement.executeQuery(strSQL1);
            while (oResultSet.next()) {
                aloBean.add(this.get(new LineadepedidoBean(oResultSet.getInt("id")), AppConfigurationHelper.getJsonMsgDepth()));
            }
        } catch (Exception ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName();
            Log4j.errorLog(msg, ex);
            throw new Exception(msg, ex);
        } finally {
            if (oResultSet != null) {
                oResultSet.close();
            }
            if (oPreparedStatement != null) {
                oPreparedStatement.close();
            }
        }
        return aloBean;
    }
}
