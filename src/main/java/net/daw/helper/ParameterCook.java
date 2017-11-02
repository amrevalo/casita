/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author a022593391p
 */
public class ParameterCook {
    public static String prepareCamelCaseObject(HttpServletRequest request) {
        String result = null;
        if (request.getParameter("ob") == null) {
            result = null;
        } else {
            result = Character.toUpperCase(request.getParameter("ob").charAt(0)) + request.getParameter("ob").substring(1);
        }
        return result;
    }
    
    
    public static LinkedHashMap<String, String> getOrderParams(String strOrder) {
        LinkedHashMap<String, String> oHMOrder = new LinkedHashMap<String, String>();
        if (strOrder != null && strOrder.length() > 0) {
            String[] arrOrderSplit1 = strOrder.split(" ");
            for (String s : arrOrderSplit1) {
                String[] arrOrderSplit2 = s.split(",");
                if (s.contains(",")) {
                    if ("asc".equalsIgnoreCase(arrOrderSplit2[1])) {
                        oHMOrder.put(arrOrderSplit2[0], "ASC");
                    } else {
                        oHMOrder.put(arrOrderSplit2[0], "DESC");
                    }
                } else {
                    oHMOrder.put(arrOrderSplit2[0], "ASC");
                }
            }
        } else {
            oHMOrder = null;
        }
        return oHMOrder;
    }

    public static ArrayList<FilterBeanHelper> getFilterParams(String strFilter) {
        ArrayList<FilterBeanHelper> oFilterBean = new ArrayList<>();
        if (strFilter != null && strFilter.length() > 0) {
            String[] arrFilterSplit1 = strFilter.split(" ");
            for (String s : arrFilterSplit1) {
                String[] arrFilterSplit2 = s.split(",");
                if (arrFilterSplit2.length == 4) {
                    FilterBeanHelper oFilterBeanHelper = new FilterBeanHelper();
                    oFilterBeanHelper.setLink(arrFilterSplit2[0]);
                    oFilterBeanHelper.setField(arrFilterSplit2[1]);
                    oFilterBeanHelper.setOperation(arrFilterSplit2[2]);
                    oFilterBeanHelper.setValue(arrFilterSplit2[3]);
                    oFilterBean.add(oFilterBeanHelper);
                }
            }
        }
        return oFilterBean;
    }
   
    
}
