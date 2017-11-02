/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.daw.beans;

import com.google.gson.annotations.Expose;

/**
 *
 * @author alumno
 */
public class ProductoBean {
    
    @Expose
    private Integer id;
    @Expose
    private Integer cantidad;
    @Expose
    private Double precio;
    @Expose
    private String nombre;
    

    
    public ProductoBean() {
 
    }
    
    public ProductoBean(Integer id) {
        this.id = id;
    }

    public ProductoBean(Integer id, Integer cantidad, Double precio, String nombre) {
        this.id = id;
        this.cantidad = cantidad;
        this.precio = precio;
        this.nombre = nombre;
    }

    public ProductoBean(Integer id, Double precio, String nombre) {
        this.id = id;
        this.precio = precio;
        this.nombre = nombre;
    }

    public ProductoBean(Integer id, Integer cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

  

  
}
