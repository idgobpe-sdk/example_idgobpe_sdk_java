package integration.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import pe.gob.id.sdk.IDGobPeClient;
import pe.gob.id.sdk.dto.User;

/**
 * @author Alexander Llacho
 */
@Controller
public class HomeController extends ParentController {

    @GetMapping("/home")
    public ModelAndView getIndex(
            @SessionAttribute("oUser") User oUser
    ) {
        ModelAndView response = new ModelAndView("home");

        response.addObject("oUser", oUser);
        response.addObject("baseUrl", baseUrl);

        return response;
    }

    @GetMapping("/logout")
    public String logout() {
        IDGobPeClient oClient = getIdGobPeClient();
        String logoutUri = oClient.getLogoutUri(baseUrl);

        return "redirect:".concat(logoutUri);
    }
}
