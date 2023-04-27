package integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import pe.gob.id.sdk.IDGobPeClient;
import pe.gob.id.sdk.dto.TokenResponse;
import pe.gob.id.sdk.dto.User;
import pe.gob.id.sdk.utils.ConvertResponse;
import pe.gob.id.sdk.utils.MySSLConnectionSocketFactory;

import javax.servlet.http.HttpSession;
import java.io.*;

/**
 * @author Alexander Llacho
 */
@Controller
public class IndexController extends ParentController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/")
    public ModelAndView getIndex(HttpSession session) throws IOException {
        ModelAndView response = new ModelAndView("index");
        IDGobPeClient oClient = getIdGobPeClient();

        session.setAttribute("session_state", oClient.getState());
        response.addObject("url", oClient.getLoginUrl());

        return response;
    }

    @GetMapping("/auth-endpoint")
    public String getAuthEndpoint(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @SessionAttribute(value = "session_state", required = false) String sessionState,
            HttpSession session
    ) {
        try {
            if (error == null && code != null) {
                if (state.equals(sessionState)) {
                    IDGobPeClient oClient = getIdGobPeClient();
                    TokenResponse oTokenResponse = oClient.getTokens(code);

                    if (oTokenResponse != null) {
                        User oUser = oClient.getUserInfo(oTokenResponse.getAccessToken());

                        session.setAttribute("oUser", oUser);

                        return "redirect:/home";
                    }
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));

            logger.error(sw.toString());
        }

        return "redirect:".concat(baseUrl);
    }
}
