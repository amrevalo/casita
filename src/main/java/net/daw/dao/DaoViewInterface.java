/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import net.daw.helper.FilterBeanHelper;

/**
 *
 * @author alumno
 */
public interface DaoViewInterface <GenericBeanImplementation>{
    
    public Long getCount(ArrayList<FilterBeanHelper> alFilter) throws Exception;
 
    public ArrayList<GenericBeanImplementation> getPage(int intRegsPerPag, int intPage, LinkedHashMap<String,String> hmOrder, ArrayList<FilterBeanHelper> alFilter) throws Exception;
    
}
