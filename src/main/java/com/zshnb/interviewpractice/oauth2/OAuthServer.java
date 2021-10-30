package com.zshnb.interviewpractice.oauth2;

import com.nimbusds.oauth2.sdk.*;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/oauth2")
public class OAuthServer {
    private static List<String> clientIds = new ArrayList<>();

    @GetMapping("/register-client")
    public HttpServletResponse registerClient(@RequestParam String clientId,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {
        clientIds.add(clientId);
        response.setStatus(200);
        return response;
    }

    @GetMapping("/authorization")
    public AuthorizationResponse authorization(@RequestParam String clientId,
                                               @RequestParam String scope,
                                               @RequestParam String callback) throws URISyntaxException, ParseException {
        URI endPoint = new URI("http://localhost");
        URI callBackUri = new URI(callback);
        State state = new State();
        AuthorizationRequest request = new AuthorizationRequest.Builder(ResponseType.CODE, new ClientID(clientId))
            .endpointURI(endPoint)
            .scope(new Scope(scope))
            .state(state)
            .redirectionURI(callBackUri).build();

        AuthorizationResponse response = AuthorizationResponse.parse(request.toURI());
        if (response.indicatesSuccess()) {
            if (request.getResponseType().equals(ResponseType.CODE)) {
                AuthorizationCode code = new AuthorizationCode();
                return new AuthorizationSuccessResponse(callBackUri, code, null, state, ResponseMode.QUERY);
            } else {
                return response;
            }
        } else {
            return response.toErrorResponse();
        }
    }
}
