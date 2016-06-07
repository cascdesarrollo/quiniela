/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frontino.quiniela.services;

import com.frontino.quiniela.entidades.MarcadorQuinielas;
import com.frontino.quiniela.entidades.Partidos;
import com.frontino.quiniela.entidades.Quinielas;
import com.frontino.quiniela.entidades.Usuarios;
import com.frontino.quiniela.logica.seguridad.Authenticator;
import com.frontino.quiniela.logica.seguridad.SessionDto;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 *
 * @author casc
 */
@Stateless
@Path("quinielas")
public class QuinielasFacadeREST extends AbstractFacade<Quinielas> {

    @EJB
    private MarcadorQuinielasFacadeREST marcadorQuinielasFacadeREST;

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
    @Path("consultar")
    @Produces({"application/xml", "application/json"})
    public List<Quinielas> consultar(
            @QueryParam("s") String _status
    ) {
        StringBuilder condi = new StringBuilder();
        if (_status != null && !_status.equals("A")) {
            condi.append(" WHERE status='").append(_status)
                    .append("'");
        }
        Usuarios usuario = null;
        Query q = em.createQuery(new StringBuffer("select p ") //
                .append(" from Quinielas p")
                .append(condi.toString())
                .append(" ORDER BY p.acumulado DESC, p.alias")
                .toString()
        );
        return q.getResultList();
    }

    @GET
    @Path("nuevadetalle")
    @Produces("application/json")
    public List<MarcadorQuinielas> nuevaQuinielaDetalle(
            @Context HttpServletRequest _request,
            @QueryParam("valida") String _token
    ) {
        List<MarcadorQuinielas> arr = new ArrayList<>();
        Authenticator autenticador = Authenticator.getInstance();
        String sessionID = _token.split("-")[0];
        _token = _token.substring(sessionID.length() + 1, _token.length());
        try {
            if (autenticador.isAuthTokenValid(sessionID, _token)) {
                Query q = em.createQuery(new StringBuffer("select p ") //
                        .append(" FROM Partidos p")
                        .append(" ORDER BY p.idGrupo.id, p.fecha")
                        .toString());
                List<Partidos> arrPartidos = q.getResultList();
                MarcadorQuinielas marca;
                for (Partidos partido : arrPartidos) {
                    marca = new MarcadorQuinielas();
                    marca.setIdPartido(partido);
                    marca.setGolEquipo1(0);
                    marca.setGolEquipo2(0);
                    arr.add(marca);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(QuinielasFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arr;
    }

    @POST
    @Path("registrar")
    @Consumes("application/json")
    public Response registrarQuiniela(
            @QueryParam("valida") String _token,
            @QueryParam("alias") String _alias,
            List<MarcadorQuinielas> _detalle
    ) {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("error", true);
            jsonObjBuilder.add("des_error", "Lo Siento Ya comenzo la copa");
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
            /*
        Authenticator autenticador = Authenticator.getInstance();
        String sessionID = _token.split("-")[0];
        _token = _token.substring(sessionID.length() + 1, _token.length());
        try {
            if (autenticador.isAuthTokenValid(sessionID, _token)) {
                SessionDto usuario = autenticador.getSession(sessionID, _token);
                Quinielas quiniela = new Quinielas();
                quiniela.setAlias(_alias);
                quiniela.setAcumulado(0);
                quiniela.setStatus("P");
                quiniela.setFecharegistro(new Date());
                quiniela.setIdUsuario(usuario.getUsuario());
                for (MarcadorQuinielas linea : _detalle) {
                    linea.setIdQuiniela(quiniela);
                    linea.setPuntos(0);
                    linea.setAcumulado(0);
                }
                quiniela.setMarcadorQuinielasCollection(_detalle);
                super.create(quiniela);
            }
        } catch (Exception ex) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("error", true);
            jsonObjBuilder.add("des_error", "Error Validando Alias " + ex.getMessage());
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder(Response.Status.CONFLICT).entity(jsonObj.toString()).build();
        }
        //autenticador.logout(null, null);
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("message", "registrar");
        JsonObject jsonObj = jsonObjBuilder.build();
        return getNoCacheResponseBuilder(Response.Status.ACCEPTED).entity(jsonObj.toString()).build();
            */
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @GET
    @Path("validaalias")
    @Produces("application/json")
    public Response validaAlias(
            @Context HttpServletRequest _request,
            @QueryParam("valida") String _token,
            @QueryParam("alias") String _alias
    ) {
        Authenticator autenticador = Authenticator.getInstance();
        String sessionID = _request.getSession().getId();
        try {
            if (autenticador.isAuthTokenValid(sessionID, _token)) {
                Query q = em.createQuery(new StringBuffer("select p ") //
                        .append(" from Quinielas p") //
                        .append(" where p.alias=?1").toString());
                q.setParameter(1, _alias);
                if (q.getResultList().size() > 0) {
                    JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
                    jsonObjBuilder.add("error", true);
                    jsonObjBuilder.add("des_error", "Ya existe una quiniela registrada bajo este nombre " + _alias);
                    JsonObject jsonObj = jsonObjBuilder.build();
                    return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
                }
            }
        } catch (Exception ex) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("error", true);
            jsonObjBuilder.add("des_error", "Error Validando Alias " + ex.getMessage());
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
        }
        //autenticador.logout(null, null);
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("message", "Executed demoPostMethod");
        JsonObject jsonObj = jsonObjBuilder.build();

        return getNoCacheResponseBuilder(Response.Status.ACCEPTED).entity(jsonObj.toString()).build();
    }

    private Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status) {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);
        return Response.status(status).cacheControl(cc);
    }

    @GET
    @Path("detallequiniela")
    @Produces("application/json")
    public List<MarcadorQuinielas> consultarDetalleQuiniela(
            @Context HttpServletRequest _request,
            @QueryParam("id") int _id
    ) {
        return marcadorQuinielasFacadeREST.consultarDetalle(_id);
    }

    @GET
    @Path("quinielausuario")
    @Produces({"application/xml", "application/json"})
    public List<Quinielas> consultarQuinielasUsuario(
            @QueryParam("valida") String _token
    ) {
        Authenticator autenticador = Authenticator.getInstance();
        String sessionID = _token.split("-")[0];
        _token = _token.substring(sessionID.length() + 1, _token.length());
        try {
            SessionDto usuario = autenticador.getSession(sessionID, _token);
            if (usuario != null) {
                Query q = em.createQuery(new StringBuffer("select p ") //
                        .append(" from Quinielas p")
                        .append(" WHERE p.idUsuario=?1")
                        .append(" ORDER BY p.acumulado DESC, p.alias")
                        .toString()
                );
                q.setParameter(1, usuario.getUsuario());
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

    @POST
    @Path("editar")
    @Consumes("application/json")
    public Response editarQuiniela(
            @QueryParam("valida") String _token,
            List<MarcadorQuinielas> _detalle
    ) {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("error", true);
            jsonObjBuilder.add("des_error", "Ya comenzo la copa");
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
        /*
        Authenticator autenticador = Authenticator.getInstance();
        String sessionID = _token.split("-")[0];
        _token = _token.substring(sessionID.length() + 1, _token.length());
        try {
            if (autenticador.isAuthTokenValid(sessionID, _token)) {
                for (MarcadorQuinielas linea : _detalle) {
                    marcadorQuinielasFacadeREST.edit(linea.getId(), linea);
                }
            }
        } catch (Exception ex) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("error", true);
            jsonObjBuilder.add("des_error", "Error Validando Alias " + ex.getMessage());
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
        }
        //autenticador.logout(null, null);
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("message", "Executed demoPostMethod");
        JsonObject jsonObj = jsonObjBuilder.build();

        return getNoCacheResponseBuilder(Response.Status.ACCEPTED).entity(jsonObj.toString()).build();
                */
    }

}
