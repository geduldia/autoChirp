package autoChirp.webController;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("twitter")
public class TweetsController {

private ConnectionRepository connectionRepository;
private Twitter twitterConnection;

@Inject
public TweetsController(ConnectionRepository connectionRepository, Twitter twitterConnection) {
        this.connectionRepository = connectionRepository;
        this.twitterConnection = twitterConnection;
}

@RequestMapping(value = "/dashboard")
public ModelAndView dashboard(Model model) {
        if (!model.containsAttribute("twitter"))
                return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("dashboard");
        return mv;
}

@RequestMapping(value = "/groups")
public ModelAndView groups(Model model) {
        if (!model.containsAttribute("twitter"))
                return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("groups");
        return mv;
}

@RequestMapping(value = "/groups/add")
public ModelAndView addGroup(Model model) {
        if (!model.containsAttribute("twitter"))
                return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("group");
        return mv;
}

@RequestMapping(value = "/groups/edit")
public ModelAndView editGroup(Model model) {
        if (!model.containsAttribute("twitter"))
                return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("group");
        return mv;
}

@RequestMapping(value = "/groups/import")
public ModelAndView importGroup(Model model) {
        if (!model.containsAttribute("twitter"))
                return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("import");
        return mv;
}

@RequestMapping(value = "/tweets")
public ModelAndView tweets(Model model) {
        if (!model.containsAttribute("twitter"))
                return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("tweets");
        return mv;
}

@RequestMapping(value = "/tweets/add")
public ModelAndView addTweet(Model model) {
        if (!model.containsAttribute("twitter"))
                return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("tweet");
        return mv;
}

@RequestMapping(value = "/tweets/edit")
public ModelAndView editTweet(Model model) {
        if (!model.containsAttribute("twitter"))
                return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("tweet");
        return mv;
}

@RequestMapping(value = "/tweets/direct")
public ModelAndView tweet(Model model) {
        if (!model.containsAttribute("twitter"))
                return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("direct");
        return mv;
}

}
