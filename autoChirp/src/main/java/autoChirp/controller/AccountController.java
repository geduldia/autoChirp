package autoChirp.controller;

import java.util.Hashtable;
import javax.inject.Inject;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("twitterAccount")
public class AccountController {

private ConnectionRepository connectionRepository;
private Twitter twitter;

@Inject
public AccountController(ConnectionRepository connectionRepository, Twitter twitter) {
        this.connectionRepository = connectionRepository;
        this.twitter = twitter;
}

@RequestMapping(value = "/account")
public ModelAndView account() {
        ModelAndView mv = new ModelAndView("account");

        if (connectionRepository.findPrimaryConnection(Twitter.class) != null) {
                UserOperations userOperations = twitter.userOperations();
                TwitterProfile twitterProfile = userOperations.getUserProfile();

                Hashtable twitterAccount = new Hashtable();
                twitterAccount.put("auth", true);
                twitterAccount.put("name", twitterProfile.getName());
                twitterAccount.put("handle", twitterProfile.getScreenName());
                twitterAccount.put("description", twitterProfile.getDescription());
                twitterAccount.put("url", twitterProfile.getProfileUrl());
                twitterAccount.put("image", twitterProfile.getProfileImageUrl());
                twitterAccount.put("protected", twitterProfile.isProtected());

                mv.addObject("twitterAccount", twitterAccount);
        }

        return mv;
}

}
