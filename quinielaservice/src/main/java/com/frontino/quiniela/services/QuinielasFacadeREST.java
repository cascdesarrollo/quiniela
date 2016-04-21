/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frontino.quiniela.services;

import com.frontino.quiniela.entidades.MarcadorQuinielas;
import com.frontino.quiniela.entidades.Partidos;
import com.frontino.quiniela.entidades.Quinielas;
import com.frontino.quiniela.logica.seguridad.Authenticator;
import com.frontino.quiniela.logica.seguridad.SessionDto;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 *
 * @author casc
 */
@Stateless
@Path("quinielas")
public class QuinielasFacadeREST extends AbstractFacade<Quinielas> {
 
    @Context
    private HttpServletRequest request;
    
    @EJB
    private PartidosFacadeREST partidosFacadeREST;

    @PersistenceContext(unitName = "com.frontino_quinielaservice_war_1.0PU")
    private EntityManager em;

    public QuinielasFacadeREST() {
        super(Quinielas.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Quinielas entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, Quinielas entity) {
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
    public Quinielas find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Quinielas> findAll() {
        return super.findAll();
    }

    @GET
    @Path("nueva")
    @Produces("application/json")
    public Quinielas nuevaQuiniela(
            @QueryParam("valida") String _token
    ) {
        Quinielas quiniela = new Quinielas();
        quiniela.setAlias("Alias de pruebas");
        quiniela.setAcumulado(0);
        Authenticator autenticador = Authenticator.getInstance();
        
        String sessionID2 = request.getSession().getId();
        System.out.println("OBTENIENDO DATOS " + sessionID2);
        
        String sessionID = "1234567890";
        
        try {
            SessionDto usuario = autenticador.getSession(sessionID, _token);
            List<Partidos> arrPartidos = partidosFacadeREST.findAll();
            List<MarcadorQuinielas> arr = new ArrayList<>();
            MarcadorQuinielas marca;
            for (Partidos partido : arrPartidos) {
                marca = new MarcadorQuinielas();
                marca.setIdPartido(partido);
                marca.setGolEquipo1(0);
                marca.setGolEquipo2(0);
                arr.add(marca);
            }
            quiniela.setMarcadorQuinielasCollection(arr);
        } catch (Exception ex) {
            Logger.getLogger(QuinielasFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return quiniela;
    }

    @GET
    @Path("nuevadetalle")
    @Produces("application/json")
    public List<MarcadorQuinielas> nuevaQuinielaDetalle(
            @QueryParam("valida") String _token
    ) {
        List<MarcadorQuinielas> arr = new ArrayList<>();
        Authenticator autenticador = Authenticator.getInstance();
        //String sessionID = request.getSession().getId();
        String sessionID = "1234567890";
        System.out.println("OBTENIENDO DATOS " + sessionID);
        try {
            SessionDto usuario = autenticador.getSession(sessionID, _token);
            List<Partidos> arrPartidos = partidosFacadeREST.findAll();

            MarcadorQuinielas marca;
            for (Partidos partido : arrPartidos) {
                marca = new MarcadorQuinielas();
                marca.setIdPartido(partido);
                marca.setGolEquipo1(0);
                marca.setGolEquipo2(0);
                arr.add(marca);
            }
        } catch (Exception ex) {
            Logger.getLogger(QuinielasFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arr;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}