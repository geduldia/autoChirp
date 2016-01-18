package autoChirp.webController;

import java.util.Hashtable;
import javax.inject.Inject;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("twitter")
public class AccountController {

private ConnectionRepository connectionRepository;
private Twitter twitterConnection;

@Inject
public AccountController(ConnectionRepository connectionRepository, Twitter twitterConnection) {
        this.connectionRepository = connectionRepository;
        this.twitterConnection = twitterConnection;
}

@RequestMapping(value = "/account")
public ModelAndView account() {
        ModelAndView mv = new ModelAndView("account");
        return mv;
}

@RequestMapping(value = "/account/login")
public String login(Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) != null) {
                UserOperations userOperations = twitterConnection.userOperations();
                TwitterProfile twitterProfile = userOperations.getUserProfile();

                Hashtable twitter = new Hashtable();
                twitter.put("name", twitterProfile.getName());
                twitter.put("handle", twitterProfile.getScreenName());
                twitter.put("description", twitterProfile.getDescription());
                twitter.put("url", twitterProfile.getProfileUrl());
                twitter.put("image", twitterProfile.getProfileImageUrl());
                twitter.put("protected", twitterProfile.isProtected());

                model.addAttribute("twitter", twitter);
        }

        return "redirect:/account";
}

@RequestMapping(value = "/account/logout")
public String logout(SessionStatus sessionStatus) {
        connectionRepository.removeConnections("twitter");
        sessionStatus.setComplete();

        return "redirect:/account";
}

@RequestMapping(value = "/account/delete")
public String delete() {
        return "redirect:/account";
}

}
