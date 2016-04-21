package com.frontino.quiniela.logica.seguridad;

import com.frontino.quiniela.entidades.Usuarios;
import com.frontino.quiniela.services.UsuariosFacadeREST;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.security.auth.login.LoginException;

@Stateless
public class Authenticator {
    @EJB
    private UsuariosFacadeREST usuariosFacadeREST;
    private static Authenticator authenticator = null;

    // A service key storage which stores <service_key, username>
    private final Map<String, String> serviceKeysStorage = new HashMap();

    // An authentication token storage which stores <service_key, auth_token>.
    private final Map<String, SessionDto> authorizationTokensStorage = new HashMap();

    public Authenticator() {
    }

    public static Authenticator getInstance() {
        if (authenticator == null) {
            authenticator = new Authenticator();
        }

        return authenticator;
    }

    /**
     * 
     * @param _sessionID
     * @param _usuario
     * @return
     * @throws LoginException 
     */
    public String login(String _sessionID, Usuarios _usuario) throws LoginException {
        SessionDto dto = new SessionDto();
        if (_usuario != null) {
            dto.setUsuario(_usuario);
            dto.setSesionID(_sessionID);
            String authToken = UUID.randomUUID().toString();
            authorizationTokensStorage.put(authToken, dto);
            return authToken;
        } else {
            return null;
        }
    }


    /**
     *
     * @param _sessionID
     * @param authToken
     * @return
     */
    public boolean isAuthTokenValid(String _sessionID, String authToken) {
        if (authorizationTokensStorage.containsKey(authToken)) {
            SessionDto sesion = authorizationTokensStorage.get(authToken);
            if (sesion.getSesionID().equals(_sessionID)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param _sessionID
     * @param authToken
     * @throws GeneralSecurityException
     */
    public void logout(String _sessionID, String authToken) throws GeneralSecurityException {
        if (authorizationTokensStorage.containsKey(authToken)) {
            SessionDto sesion = authorizationTokensStorage.get(authToken);
            if (sesion.getSesionID().equals(_sessionID)) {
                authorizationTokensStorage.remove(authToken);
                return;
            }
        }
        throw new GeneralSecurityException("Invalid service key and authorization token match.");
    }

    /**
     *
     * @param _sessionID
     * @param _authToken
     * @return
     * @throws Exception
     */
    public SessionDto getSession(String _sessionID, String _authToken) throws Exception {
        SessionDto dto;
        if (isAuthTokenValid(_sessionID, _authToken)) {
            if (authorizationTokensStorage.containsKey(_authToken)) {
                dto = authorizationTokensStorage.get(_authToken);
                return dto;
            }
        }
        return null;
    }



   
}
