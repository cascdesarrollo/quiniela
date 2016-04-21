/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frontino.quiniela.services;

import com.frontino.quiniela.entidades.Usuarios;
import com.frontino.quiniela.logica.seguridad.Authenticator;
import com.frontino.quiniela.logica.seguridad.SessionDto;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author casc
 */
@Stateless
@Path("session")
public class SessionREST {
    @EJB
    private UsuariosFacadeREST usuariosFacadeREST;
    @PersistenceContext(unitName = "com.frontino_quinielaservice_war_1.0PU")
    private EntityManager em;
    
    private static final long serialVersionUID = -6663599014192066936L;

    /*@Context
    private HttpServletRequest request;*/

    /**
     * Creates a new instance of SessionREST
     */
    public SessionREST() {
    }

    @POST
    @Path("login")
    @Produces("application/json")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(
            @Context HttpServletRequest _request,
            @FormParam("username") String username,
            @FormParam("password") String password
    ) {
        Authenticator autenticador = Authenticator.getInstance();
        String sessionID = _request.getSession().getId();
        System.out.println("OBTENIENDO DATOS " + sessionID);
        try {
            Usuarios usuario = usuariosFacadeREST.consultarUsuario(username, stringToMd5(password));
            String authToken = autenticador.login(sessionID, usuario);
            if (authToken == null) {
                JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
                jsonObjBuilder.add("error", true);
                jsonObjBuilder.add("des_error", "Usuario o Contraseña Ivalida");
                JsonObject jsonObj = jsonObjBuilder.build();
                return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
            }
            SessionDto dataSession = autenticador.getSession(sessionID, authToken);

            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("auth_token", authToken);
            jsonObjBuilder.add("email", dataSession.getUsuario().getEmail());
            jsonObjBuilder.add("nombre", dataSession.getUsuario().getNombre());
            jsonObjBuilder.add("telefono", dataSession.getUsuario().getTelefono());
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

    /**
     *
     * @param status
     * @return
     */
    private Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status) {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);

        return Response.status(status).cacheControl(cc);
    }

    @POST
    @Path("logout")
    @Produces("application/json")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response logout(
            @Context HttpServletRequest _request,
            @FormParam("valida") String _token) {
        Authenticator autenticador = Authenticator.getInstance();
        String sessionID = _request.getSession().getId();
        System.out.println("CERRANDO DATOS " + sessionID);
        try {
            autenticador.logout(sessionID, _token);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(SessionREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        //autenticador.logout(null, null);
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("message", "Executed demoPostMethod");
        JsonObject jsonObj = jsonObjBuilder.build();

        return getNoCacheResponseBuilder(Response.Status.ACCEPTED).entity(jsonObj.toString()).build();
    }

    @GET
    @Path("dataSession")
    @Produces("application/json")
    @Consumes("application/json")
    public Response datosSession(
            @Context HttpServletRequest _request,
            @QueryParam("valida") String _token
    ) {
        Authenticator autenticador = Authenticator.getInstance();
        String sessionID = _request.getSession().getId();
        System.out.println("OBTENIENDO DATOS " + sessionID);
        try {
            SessionDto usuario = autenticador.getSession(sessionID, _token);
            if (usuario != null) {
                JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
                jsonObjBuilder.add("auth_token", _token);
                jsonObjBuilder.add("email", usuario.getUsuario().getEmail());
                jsonObjBuilder.add("nombre", usuario.getUsuario().getNombre());
                JsonObject jsonObj = jsonObjBuilder.build();
                return getNoCacheResponseBuilder(Response.Status.OK).entity(jsonObj.toString())
                        .build();
            } else {
                JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
                jsonObjBuilder.add("error", true);
                jsonObjBuilder.add("des_error", "Datos de Session No Coinciden");
                JsonObject jsonObj = jsonObjBuilder.build();
                return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
            }
        } catch (final Exception ex) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("error", true);
            jsonObjBuilder.add("des_error", "Error Obteniendo datos" + ex.getMessage());
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
        } finally {

        }
    }
    
    protected EntityManager getEntityManager() {
        return em;
    }
    
        /**
     * Convertir cadena en MD5
     *
     * @param _cadena
     * @return
     */
    private String stringToMd5(String _cadena) {
        String password = _cadena;
        StringBuilder sb = new StringBuilder();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");

            md.update(password.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Authenticator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

}
