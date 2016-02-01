package autoChirp.webController;

import autoChirp.DBConnector;
import java.util.Hashtable;
import javax.inject.Inject;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("account")
public class AccountController {

private ConnectionRepository connectionRepository;
private Twitter twitter;

@Inject
public AccountController(ConnectionRepository connectionRepository, Twitter twitter) {
        this.connectionRepository = connectionRepository;
        this.twitter = twitter;
}

@RequestMapping("/account")
public String account() {
        return "account";
}

@RequestMapping("/account/login")
public String login(Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) != null) {
                UserOperations userOperations = twitter.userOperations();
                TwitterProfile twitterProfile = userOperations.getUserProfile();
                Connection<Twitter> twitterConnection = connectionRepository.getPrimaryConnection(Twitter.class);

                long twitterID = userOperations.getProfileId();
                int userID = DBConnector.checkForUser(twitterID);

                if (userID == -1) {
                        ConnectionData twitterConnectionData = twitterConnection.createData();
                        String token = twitterConnectionData.getAccessToken();
                        String secret = twitterConnectionData.getSecret();
                        userID = DBConnector.insertNewUser(twitterID, token, secret);
                }

                Hashtable<String, String> account = new Hashtable<String, String>();
                account.put("userID", Integer.toString(userID));
                account.put("twitterID", Long.toString(twitterID));
                account.put("name", twitterProfile.getName());
                account.put("handle", twitterProfile.getScreenName());
                account.put("description", twitterProfile.getDescription());
                account.put("url", twitterProfile.getProfileUrl());
                account.put("image", twitterProfile.getProfileImageUrl());
                account.put("protected", String.valueOf(twitterProfile.isProtected()));
                model.addAttribute("account", account);
        }

        return "redirect:/account";
}

@RequestMapping("/account/logout")
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
