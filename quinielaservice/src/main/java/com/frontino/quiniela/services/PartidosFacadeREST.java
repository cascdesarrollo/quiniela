/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frontino.quiniela.services;

import com.frontino.quiniela.entidades.Partidos;
import com.frontino.quiniela.logica.Utilidades;
import com.frontino.quiniela.logica.seguridad.Authenticator;
import com.frontino.quiniela.logica.seguridad.SessionDto;
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
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author casc
 */
@Stateless
@Path("partidos")
public class PartidosFacadeREST extends AbstractFacade<Partidos> {

    @PersistenceContext(unitName = "com.frontino_quinielaservice_war_1.0PU")
    private EntityManager em;

    public PartidosFacadeREST() {
        super(Partidos.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Partidos entity) {
        super.create(entity);
    }

    @POST
    @Path("actualizar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response actualizar(
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("id") int id,
            @FormParam("equipo1") int equipo1,
            @FormParam("equipo2") int equipo2,
            @QueryParam("valida") String _token) {
        Authenticator autenticador = Authenticator.getInstance();
        String sessionID = _token.split("-")[0];
        _token = _token.substring(sessionID.length() + 1, _token.length());
        try {
            SessionDto usuario = autenticador.getSession(sessionID, _token);
            if (usuario != null) {
                if (usuario.getUsuario().getEmail().equals(username)
                        && usuario.getUsuario().getPassword().equals(Utilidades.stringToMd5(password))
                        && usuario.getUsuario().getTipo() == 'Z') {
                    Partidos partido = super.find(id);
                    if (partido != null) {
                        partido.setGolEquipo1(equipo1);
                        partido.setGolEquipo2(equipo2);
                        partido.setStatus('2');
                        super.edit(partido);
                    }
                } else {
                    JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
                    jsonObjBuilder.add("error", true);
                    jsonObjBuilder.add("des_error", "Error Validando Credenciales");
                    JsonObject jsonObj = jsonObjBuilder.build();
                    return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
                }
            } else {
                JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
                jsonObjBuilder.add("error", true);
                jsonObjBuilder.add("des_error", "Error Consultado Identificador de Partido");
                JsonObject jsonObj = jsonObjBuilder.build();
                return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
            }
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("exito", "exito");
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder(Response.Status.OK).entity(jsonObj.toString())
                    .build();
        } catch (final Exception ex) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("error", true);
            jsonObjBuilder.add("des_error", "Error Validando crenciales " + ex.getMessage());
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
        } finally {

        }
    }

    @GET
    @Path("pendientes")
    @Produces({"application/xml", "application/json"})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public List<Partidos> consultarPartidos(
            @QueryParam("valida") String _token
    ) {
        Authenticator autenticador = Authenticator.getInstance();
        String sessionID = _token.split("-")[0];
        _token = _token.substring(sessionID.length() + 1, _token.length());
        try {
            SessionDto usuario = autenticador.getSession(sessionID, _token);
            if (usuario != null) {
                Query q = em.createQuery(new StringBuffer("select p ") //
                        .append(" from Partidos p")
                        .append(" WHERE p.status='1'")
                        .append(" ORDER BY p.fecha, p.id")
                        .toString()
                );
                return q.getResultList();
            } else {
                System.out.println("Usuario NULL");
                return null;
            }
        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {

        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Partidos> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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

    private Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status) {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);

        return Response.status(status).cacheControl(cc);
    }

}
