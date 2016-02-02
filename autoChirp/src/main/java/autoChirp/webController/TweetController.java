package autoChirp.webController;

import autoChirp.DBConnector;
import autoChirp.tweetCreation.Tweet;
import autoChirp.tweetCreation.TweetGroup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"account", "config"})
public class TweetController {

@RequestMapping(value = "/dashboard")
public ModelAndView dashboard(Model model) {
        if (!model.containsAttribute("account")) return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("dashboard");
        return mv;
}

@RequestMapping(value = "/groups/view")
public ModelAndView viewGroups(Model model) {
        if (!model.containsAttribute("account")) return new ModelAndView("redirect:/account");

        List<TweetGroup> tweetGroups = new ArrayList<TweetGroup>();

        for (int i = 0; i < 10; i++)
          tweetGroups.add(new TweetGroup("String title [" + i + "]", "String description [" + i + "]", i != 5));

        ModelAndView mv = new ModelAndView("groups");
        mv.addObject("tweetGroups", tweetGroups);

        return mv;
}

@RequestMapping(value = "/groups/view/{groupID}")
public ModelAndView viewGroup(Model model, @PathVariable int groupID) {
        if (!model.containsAttribute("account")) return new ModelAndView("redirect:/account");

        TweetGroup tweetGroup = new TweetGroup("String title", "String description", true);
        List<Tweet> tweetsList = new ArrayList<Tweet>();

        for (int i = 0; i < 20; i++)
                tweetsList.add(new Tweet("String tweetDate [" + i + "]", "String content [" + i + "]", i, true, false));

        ModelAndView mv = new ModelAndView("group");
        mv.addObject("tweetGroup", tweetGroup);
        mv.addObject("tweetsList", tweetsList);

        return mv;
}

@RequestMapping(value = "/groups/add")
public ModelAndView addGroup(Model model) {
        if (!model.containsAttribute("account")) return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("group");
        return mv;
}

@RequestMapping(value = "/groups/import")
public ModelAndView importGroup(Model model) {
        if (!model.containsAttribute("account")) return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("import");
        return mv;
}

@RequestMapping(value = "/groups/edit/{groupID}")
public ModelAndView editGroup(Model model, @PathVariable int groupID) {
        if (!model.containsAttribute("account")) return new ModelAndView("redirect:/account");

        TweetGroup tweetGroup = new TweetGroup("String title", "String description", true);

        ModelAndView mv = new ModelAndView("group");
        mv.addObject("tweetGroup", tweetGroup);

        return mv;
}

@RequestMapping(value = "/groups/delete/{groupID}")
public String deleteGroup(Model model, @PathVariable int groupID) {
        if (!model.containsAttribute("account")) return "redirect:/account";

        return "redirect:/groups/view";
}

@RequestMapping(value = "/tweets/view")
public ModelAndView viewTweets(Model model, HttpSession session) {
        if (!model.containsAttribute("account")) return new ModelAndView("redirect:/account");
        int userID = Integer.parseInt(((Hashtable<String,String>) session.getAttribute("account")).get("userID"));
        System.out.println(userID);
        
        List<Tweet> tweetsList = DBConnector.getTweetsForUser(userID,true, false);

        ModelAndView mv = new ModelAndView("tweets");
        mv.addObject("tweetsList", tweetsList);

        return mv;
}

@RequestMapping(value = "/tweets/view/{tweetID}")
public ModelAndView viewTweet(Model model, @PathVariable int tweetID) {
        if (!model.containsAttribute("account")) return new ModelAndView("redirect:/account");

        Tweet tweetEntry = new Tweet("String tweetDate", "String content", tweetID, true, true);

        ModelAndView mv = new ModelAndView("tweet");
        mv.addObject("tweetEntry", tweetEntry);

        return mv;
}

@RequestMapping(value = "/tweets/add")
public ModelAndView addTweet(Model model) {
        if (!model.containsAttribute("account")) return new ModelAndView("redirect:/account");

        List<TweetGroup> tweetGroups = new ArrayList<TweetGroup>();

        for (int i = 0; i < 10; i++)
          tweetGroups.add(new TweetGroup("String title [" + i + "]", "String description [" + i + "]"));

        ModelAndView mv = new ModelAndView("tweet");
        mv.addObject("tweetGroups", tweetGroups);

        return mv;
}

@RequestMapping(value = "/tweets/edit/{tweetID}")
public ModelAndView editTweet(Model model, @PathVariable int tweetID) {
        if (!model.containsAttribute("account")) return new ModelAndView("redirect:/account");

        Tweet tweetEntry = new Tweet("String tweetDate", "String content", tweetID, true, true);
        List<TweetGroup> tweetGroups = new ArrayList<TweetGroup>();

        for (int i = 0; i < 10; i++)
          tweetGroups.add(new TweetGroup("String title [" + i + "]", "String description [" + i + "]"));

        ModelAndView mv = new ModelAndView("tweet");
        mv.addObject("tweetGroups", tweetGroups);
        mv.addObject("tweetEntry", tweetEntry);

        return mv;
}

@RequestMapping(value = "/tweets/delete/{tweetID}")
public String deleteTweet(Model model, @PathVariable int tweetID) {
        if (!model.containsAttribute("account")) return "redirect:/account";

        return "redirect:/tweets/view";
}

}
