package autoChirp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PrototypeController {

	
	TweetFactory tf = new TweetFactory();

	@RequestMapping(value = "/", method = RequestMethod.GET, params = "url")
	public @ResponseBody String getTweets(@RequestParam("url") String url) {		
		StringBuffer toReturn = new StringBuffer();	
		WikipediaParser parser = new WikipediaParser();
		Document doc = parser.parse(url);	
		SentenceSplitter st = new SentenceSplitter(doc.getLanguage());	
		doc.setSentences(st.splitIntoSentences(doc.getText(), doc.getLanguage()));	
		Map<String, List<String>> tweetsByDate = tf.getTweets(doc);	
		for (String date : tweetsByDate.keySet()) {
			List<String> sentences = tweetsByDate.get(date);
			toReturn.append("DATE: " + date+"\n");
			for (String s : sentences) {
				toReturn.append("TWEET: " + s+"\n");
			}
			toReturn.append("\n");
		}
		return toReturn.toString();
	}
}
