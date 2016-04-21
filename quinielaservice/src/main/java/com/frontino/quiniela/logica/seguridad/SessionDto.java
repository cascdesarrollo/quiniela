/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frontino.quiniela.logica.seguridad;

import com.frontino.quiniela.entidades.Usuarios;

/**
 *
 * @author casc
 */
public class SessionDto {
    private Usuarios usuario;
    private String sesionID;

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getSesionID() {
        return sesionID;
    }

    public void setSesionID(String sesionID) {
        this.sesionID = sesionID;
    }
    
}
