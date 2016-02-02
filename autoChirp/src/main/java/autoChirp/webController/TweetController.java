package autoChirp.webController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import autoChirp.DBConnector;
import autoChirp.tweetCreation.Tweet;
import autoChirp.tweetCreation.TweetGroup;

@Controller
@SessionAttributes({"account", "config"})
public class TweetController {

@RequestMapping(value = "/dashboard")
public ModelAndView dashboard(HttpSession session) {
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("dashboard");
        return mv;
}

@RequestMapping(value = "/groups/view")
public ModelAndView viewGroups(HttpSession session) {

  System.out.println("TweetsController:");
  System.out.println(session.getAttribute("account") == null);

        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");

        List<TweetGroup> tweetGroups = new ArrayList<TweetGroup>();

        for (int i = 0; i < 10; i++)
          tweetGroups.add(new TweetGroup("String title [" + i + "]", "String description [" + i + "]", i != 5));

        ModelAndView mv = new ModelAndView("groups");
        mv.addObject("tweetGroups", tweetGroups);

        return mv;
}

@RequestMapping(value = "/groups/view/{groupID}")
public ModelAndView viewGroup(HttpSession session, @PathVariable int groupID) {
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");

        TweetGroup tweetGroup = new TweetGroup("String title", "String description", true);
        List<Tweet> tweetsList = new ArrayList<Tweet>();

        for (int i = 0; i < 20; i++)
                tweetsList.add(new Tweet("String tweetDate [" + i + "]", "String content [" + i + "]", i,0, true, false));

        ModelAndView mv = new ModelAndView("group");
        mv.addObject("tweetGroup", tweetGroup);
        mv.addObject("tweetsList", tweetsList);

        return mv;
}

@RequestMapping(value = "/groups/add")
public ModelAndView addGroup(HttpSession session) {
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("group");
        return mv;
}

@RequestMapping(value = "/groups/import")
public ModelAndView importGroup(HttpSession session) {
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");

        ModelAndView mv = new ModelAndView("import");
        return mv;
}

@RequestMapping(value = "/groups/edit/{groupID}")
public ModelAndView editGroup(HttpSession session, @PathVariable int groupID) {
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");

        TweetGroup tweetGroup = new TweetGroup("String title", "String description", true);

        ModelAndView mv = new ModelAndView("group");
        mv.addObject("tweetGroup", tweetGroup);

        return mv;
}

@RequestMapping(value = "/groups/delete/{groupID}")
public String deleteGroup(HttpSession session, @PathVariable int groupID) {
        if (session.getAttribute("account") == null) return "redirect:/account";

        return "redirect:/groups/view";
}

@RequestMapping(value = "/tweets/view")
public ModelAndView viewTweets(HttpSession session) {
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");
        int userID = Integer.parseInt(((Hashtable<String,String>) session.getAttribute("account")).get("userID"));
        System.out.println(userID);

        List<Tweet> tweetsList = DBConnector.getTweetsForUser(userID,true, false);
        Map<Tweet,String> tweetsByGroupNames = new TreeMap<Tweet,String>();
        for (Tweet tweet : tweetsList) {
			String groupTitle = DBConnector.getTweetGroupForUser(userID, tweet.groupID).title;
			tweetsByGroupNames.put(tweet, groupTitle);
		}

/////////ONLY FPR TESTING//////
        Tweet tweet = new Tweet("date", "blabla", 1, 1, true, false);
        tweetsByGroupNames.put(tweet, "title");
//////////////////////////////////


        ModelAndView mv = new ModelAndView("tweets");
        mv.addObject("tweetsList", tweetsByGroupNames);
        return mv;
}

@RequestMapping(value = "/tweets/view/{tweetID}")
public ModelAndView viewTweet(HttpSession session, @PathVariable int tweetID) {
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");

        Tweet tweetEntry = new Tweet("String tweetDate", "String content", tweetID,0, true, true);

        ModelAndView mv = new ModelAndView("tweet");
        mv.addObject("tweetEntry", tweetEntry);

        return mv;
}

@RequestMapping(value = "/tweets/add")
public ModelAndView addTweet(HttpSession session) {
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");

        List<TweetGroup> tweetGroups = new ArrayList<TweetGroup>();

        for (int i = 0; i < 10; i++)
          tweetGroups.add(new TweetGroup("String title [" + i + "]", "String description [" + i + "]"));

        ModelAndView mv = new ModelAndView("tweet");
        mv.addObject("tweetGroups", tweetGroups);

        return mv;
}

@RequestMapping(value = "/tweets/edit/{tweetID}")
public ModelAndView editTweet(HttpSession session, @PathVariable int tweetID) {
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");

        Tweet tweetEntry = new Tweet("String tweetDate", "String content", tweetID,0, true, true);
        List<TweetGroup> tweetGroups = new ArrayList<TweetGroup>();

        for (int i = 0; i < 10; i++)
          tweetGroups.add(new TweetGroup("String title [" + i + "]", "String description [" + i + "]"));

        ModelAndView mv = new ModelAndView("tweet");
        mv.addObject("tweetGroups", tweetGroups);
        mv.addObject("tweetEntry", tweetEntry);

        return mv;
}

@RequestMapping(value = "/tweets/delete/{tweetID}")
public String deleteTweet(HttpSession session, @PathVariable int tweetID) {
        if (session.getAttribute("account") == null) return "redirect:/account";

        return "redirect:/tweets/view";
}

}
