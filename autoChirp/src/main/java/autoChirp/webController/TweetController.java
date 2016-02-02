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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import autoChirp.DBConnector;
import autoChirp.tweetCreation.Tweet;
import autoChirp.tweetCreation.TweetGroup;

@Controller
@RequestMapping(value = "/tweets")
public class TweetController {
	
	
	private int tweetsPerPage = 15;

	@RequestMapping(value = "/view")
public ModelAndView viewTweets(HttpSession session, @RequestParam(value = "page", defaultValue = "1") String pageParam) {
		int page = Integer.parseInt(pageParam);
		int offset = (page-1)*15;
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");
        int userID = Integer.parseInt(((Hashtable<String,String>) session.getAttribute("account")).get("userID"));
        List<Tweet> tweetsList = DBConnector.getTweetsForUser(userID, true, false, offset, tweetsPerPage);
  

        ModelAndView mv = new ModelAndView("tweets");
        mv.addObject("tweetsList", tweetsList);
        return mv;
}

@RequestMapping(value = "/view/{tweetID}")
public ModelAndView viewTweet(HttpSession session, @PathVariable int tweetID) {
        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");

      //  Tweet tweet = DBConnector.getTweetByID(tweetID);
        
        Tweet tweet = new Tweet("date"+tweetID, "content", tweetID, tweetID, false, false);

        ModelAndView mv = new ModelAndView("tweet");
        mv.addObject("tweetEntry", tweet);

        return mv;
}

//@RequestMapping(value = "/add")
//public ModelAndView addTweet(HttpSession session) {
//        if (session.getAttribute("account") == null) return new ModelAndView("redirect:/account");
//
//        List<TweetGroup> tweetGroups = new ArrayList<TweetGroup>();
//
//        for (int i = 0; i < 10; i++)
//          tweetGroups.add(new TweetGroup("String title [" + i + "]", "String description [" + i + "]"));
//
//        ModelAndView mv = new ModelAndView("tweet");
//        mv.addObject("tweetGroups", tweetGroups);
//
//        return mv;
//}

@RequestMapping(value = "/edit/{tweetID}")
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

@RequestMapping(value = "/delete/{tweetID}")
public String deleteTweet(HttpSession session, @PathVariable int tweetID) {
        if (session.getAttribute("account") == null) return "redirect:/account";

        return "redirect:/tweets/view";
}

}
