package autoChirp.webController;

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
import org.springframework.web.servlet.ModelAndView;

import autoChirp.DBConnector;

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
        	Connection<Twitter> connection = connectionRepository.getPrimaryConnection(Twitter.class);
                UserOperations userOperations = twitterConnection.userOperations();
                TwitterProfile twitterProfile = userOperations.getUserProfile();
//                Use this to extract the access token:
               long twitter_id = userOperations.getProfileId();
               int user_id = DBConnector.checkForUser(twitter_id);
               if(user_id == -1){
                	 ConnectionData twitterConnectionData =   connection.createData();
                     String token = twitterConnectionData.getAccessToken();
                     String secret = twitterConnectionData.getSecret();
                	 user_id = DBConnector.insertNewUser(twitter_id, token, secret);	
                }
            

                Hashtable<String, String> twitter = new Hashtable<String, String>();
                twitter.put("user_id", Integer.toString(user_id));
                twitter.put("twitter_id", Long.toString(twitter_id));
                twitter.put("name", twitterProfile.getName());
                twitter.put("handle", twitterProfile.getScreenName());
                twitter.put("description", twitterProfile.getDescription());
                twitter.put("url", twitterProfile.getProfileUrl());
                twitter.put("image", twitterProfile.getProfileImageUrl());
                twitter.put("protected", String.valueOf(twitterProfile.isProtected()));
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
