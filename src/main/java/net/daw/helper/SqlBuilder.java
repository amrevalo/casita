/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.helper;

import static java.lang.Math.ceil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author a022593391p
 */
public class SqlBuilder {
    
    public static String buildSqlFilter(ArrayList<FilterBeanHelper> alFilter) {
        String strSQLFilter = "";
        Iterator it = alFilter.iterator();
        while (it.hasNext()) {
            FilterBeanHelper oFilterBean = (FilterBeanHelper) it.next();
            strSQLFilter += getFilterExpression(oFilterBean);
        }
        return strSQLFilter;
    }
    
    
    
     public static String buildSqlLimit(Long intTotalRegs, Integer intRegsPerPage, Integer intPageNumber) {
        String SQLLimit = "";
        if (intRegsPerPage > 0 && intRegsPerPage < 10000) {
            if (intPageNumber > 0 && intPageNumber <= (ceil((intTotalRegs / intRegsPerPage) + 1))) {
                SQLLimit = " LIMIT " + (intPageNumber - 1) * intRegsPerPage + " , " + intRegsPerPage;
            } else {
                SQLLimit = " LIMIT 0 ";
            }
        }
        return SQLLimit;
    }
    
     
     private static String getFilterExpression(FilterBeanHelper temp) {

        switch (temp.getOperation()) {
            case "like": //like
                return temp.getLink() + " " + temp.getField() + " LIKE '%" + temp.getValue() + "%' ";
            case "nlik": //not like
                return temp.getLink() + " " + temp.getField() + " NOT LIKE '%" + temp.getValue() + "%' ";
            case "star": //starts with
                return temp.getLink() + " " + temp.getField() + " LIKE '" + temp.getValue() + "%' ";
            case "nsta": //not starts with
                return temp.getLink() + " " + temp.getField() + " NOT LIKE '" + temp.getValue() + "%' ";
            case "ends": //ends with
                return temp.getLink() + " " + temp.getField() + " LIKE '%" + temp.getValue() + "' ";
            case "nend": //not ends with
                return temp.getLink() + " " + temp.getField() + " NOT LIKE '%" + temp.getValue() + "' ";
            case "equa": //equal
                return temp.getLink() + " " + temp.getField() + " = " + temp.getValue() + " ";
            case "nequ": //not equal
                return temp.getLink() + " " + temp.getField() + " != " + temp.getValue() + " ";
            case "lowe": //lower than
                return temp.getLink() + " " + temp.getField() + " < " + temp.getValue() + " ";
            case "lequ": //lower or equal than
                return temp.getLink() + " " + temp.getField() + " <= " + temp.getValue() + " ";
            case "grea": //greater than
                return temp.getLink() + " " + temp.getField() + " > " + temp.getValue() + " ";
            case "gequ": //greater or equal than
                return temp.getLink() + " " + temp.getField() + " >= " + temp.getValue() + " ";
            default:
                throw new Error("Filter expression malformed. Operator not valid.");
        }
    }
/*
    public static String buildSqlLimit(Long intTotalRegs, Integer intRegsPerPage, Integer intPageNumber) {
        String SQLLimit = "";
        if (intRegsPerPage > 0 && intRegsPerPage < 10000) {
            if (intPageNumber > 0 && intPageNumber <= (ceil(intTotalRegs / intRegsPerPage))) {
                SQLLimit = " LIMIT " + (intPageNumber - 1) * intRegsPerPage + " , " + intRegsPerPage;
            }
        }
        return SQLLimit;
    }*/

    public static String buildSqlOrder(LinkedHashMap<String, String> hmOrder) {
        String strSQLOrder = "";
        if (hmOrder != null) {
            for (Map.Entry<String, String> entry : hmOrder.entrySet()) {
                strSQLOrder += entry.getKey();
                strSQLOrder += " ";
                strSQLOrder += entry.getValue();
                strSQLOrder += ",";
            }
            strSQLOrder = " ORDER BY " + strSQLOrder.substring(0, strSQLOrder.length() - 1);
        }
        return strSQLOrder;
    }
     
}
