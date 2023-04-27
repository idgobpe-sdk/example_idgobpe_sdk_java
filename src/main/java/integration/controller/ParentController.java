package integration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.gob.id.sdk.IDGobPeClient;
import pe.gob.id.sdk.common.Acr;
import pe.gob.id.sdk.common.Scope;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Base64;

/**
 * @author Alexander Llacho
 */
public class ParentController {

    private static final Logger logger = LoggerFactory.getLogger(ParentController.class);

    protected String baseUrl = "http://localhost:8080/example_idgobpe_sdk_java_war";

    protected IDGobPeClient getIdGobPeClient() {
        try {
            IDGobPeClient oClient = new IDGobPeClient(getClass().getClassLoader().getResource("idgobpe_config.json").getFile());
            String state = new String(Base64.getEncoder().encode(String.valueOf(System.currentTimeMillis()).getBytes()));

            oClient.setRedirectUri(baseUrl.concat("/auth-endpoint"));
            oClient.setAcr(Acr.CERTIFICATE_DNIE);
            oClient.addScope(Scope.PROFILE);
            oClient.setState(state);

            return oClient;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));

            logger.error(sw.toString());
        }

        return null;
    }
}
