/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frontino.quiniela.services;

import com.frontino.quiniela.entidades.MarcadorQuinielas;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author casc
 */
@Stateless
@Path("com.frontino.quiniela.entidades.marcadorquinielas")
public class MarcadorQuinielasFacadeREST extends AbstractFacade<MarcadorQuinielas> {

    @PersistenceContext(unitName = "com.frontino_quinielaservice_war_1.0PU")
    private EntityManager em;

    public MarcadorQuinielasFacadeREST() {
        super(MarcadorQuinielas.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(MarcadorQuinielas entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, MarcadorQuinielas entity) {
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
    public MarcadorQuinielas find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<MarcadorQuinielas> consultarDetalle(
             @QueryParam("id") int _id) {
        Query q = em.createQuery(new StringBuffer("select p ") //
                .append(" from MarcadorQuinielas p") //
                .append(" where p.idQuiniela.id=?1")
                .append(" ORDER by p.idPartido.fecha, p.idPartido.idGrupo.id, p.idPartido.id")
                .toString());
        q.setParameter(1, _id);
        return q.getResultList();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<MarcadorQuinielas> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

}
