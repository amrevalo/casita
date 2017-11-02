/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.beans;

import com.google.gson.annotations.Expose;
import java.sql.Date;

/**
 *
 * @author alumno
 */
public class PedidoBean {
    @Expose
    private Integer id;
    @Expose
    private Date fecha;
    @Expose(serialize = false)
    private Integer id_usuario = 0;
    @Expose(deserialize = false)
    private UsuarioBean obj_usuario = null;
    
    public PedidoBean() {
 
    }
    
    public PedidoBean(Integer id) {
        this.id = id;
    }

    public PedidoBean(Integer id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public PedidoBean(Date fecha) {
        this.fecha = fecha;
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public UsuarioBean getObj_usuario() {
        return obj_usuario;
    }

    public void setObj_usuario(UsuarioBean obj_usuario) {
        this.obj_usuario = obj_usuario;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    
}
