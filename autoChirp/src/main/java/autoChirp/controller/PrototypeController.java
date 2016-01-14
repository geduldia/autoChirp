package autoChirp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import autoChirp.tweetCreation.Document;
import autoChirp.tweetCreation.SentenceSplitter;
import autoChirp.tweetCreation.Tweet;
import autoChirp.tweetCreation.TweetFactory;
import autoChirp.tweetCreation.Parser.WikipediaParser;

@Controller
public class PrototypeController {

	TweetFactory tweeter = new TweetFactory();
  WikipediaParser parser = new WikipediaParser();

  @RequestMapping(value = "/proto")
	public ModelAndView proto() {
		return new ModelAndView("proto");
	}

	@RequestMapping(value = "/protoview", method = RequestMethod.GET, params = "url")
	public @ResponseBody ModelAndView protoview(@RequestParam("url") String url) {
		Document doc = parser.parse(url);
		SentenceSplitter splitter = new SentenceSplitter();
		doc.setSentences(splitter.splitIntoSentences(doc.getText(), doc.getLanguage()));
		List<Tweet> tweetsList = tweeter.getTweets(doc);
		Map<String, List<String>> tweets = new HashMap<String, List<String>>();
		for (Tweet tweet : tweetsList) {
			List<String> tweetsForDate = tweets.get(tweet.getTweetDate());
			if(tweetsForDate == null) {
				tweetsForDate = new ArrayList<String>();
			}
			tweetsForDate.add(tweet.getContent());
			tweets.put(tweet.getTweetDate(), tweetsForDate);
		}
    ModelAndView mv = new ModelAndView("protoview");
    mv.addObject("tweets", tweets);
    return mv;
	}
}
