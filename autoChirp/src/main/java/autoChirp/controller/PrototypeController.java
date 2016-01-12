package autoChirp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import autoChirp.Document;
import autoChirp.SentenceSplitter;
import autoChirp.TweetFactory;
import autoChirp.WikipediaParser;

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
		Map<String, List<String>> tweets = tweeter.getTweets(doc);
    ModelAndView mv = new ModelAndView("protoview");
    mv.addObject("tweets", tweets);
    return mv;
	}
}
