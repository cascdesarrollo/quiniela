/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frontino.quiniela.services;

import com.frontino.quiniela.entidades.Usuarios;
import com.frontino.quiniela.logica.Utilidades;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author casc
 */
@Stateless
@Path("usuarios")
public class UsuariosFacadeREST extends AbstractFacade<Usuarios> {

    @PersistenceContext(unitName = "com.frontino_quinielaservice_war_1.0PU")
    private EntityManager em;
    
    public UsuariosFacadeREST() {
        super(Usuarios.class);
    }
    
    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Usuarios entity) {
        entity.setPassword(Utilidades.stringToMd5(entity.getPassword()));
        entity.setStatus('P');
        super.create(entity);
    }
    
    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Usuarios entity) {
        super.edit(entity);
    }
    
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }
    
    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Usuarios find(@PathParam("id") Integer id) {
        return super.find(id);
    }
    
    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Usuarios> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Usuarios> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }
    
    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     *
     * @param _email
     * @param _password
     * @return
     */
    public Usuarios consultarUsuario(String _email, String _password) {
        Usuarios usuario = null;
        Query q = em.createQuery(new StringBuffer("select p ") //
                .append(" from Usuarios p") //
                .append(" where p.email=?1 and p.password=?2").toString());
        q.setParameter(1, _email);
        q.setParameter(2, _password);
        if (q.getResultList().size() > 0) {
            usuario = (Usuarios) q.getSingleResult();
        }
        return usuario;
    }
    
}
