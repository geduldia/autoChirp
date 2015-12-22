package autoChirp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.HeidelTimeStandalone;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;

/**
 * @author geduldia
 * 
 * A class to generate date-related tweets from a document using Heideltime as temporal Tagger
 *
 */

public class TweetFactory {


	/**
	 * @param document  
	 * @return tweetsByDate - a map of the detected dates and their including sentences trimmed to a tweet-length of 140 characters
	 */
	public Map<String, List<String>> getTweets(Document document) {
		Map<String, List<String>> tweetsByDate = new TreeMap<String, List<String>>();
		HeidelTimeWrapper ht = new HeidelTimeWrapper(document.getLanguage(), DocumentType.NARRATIVES, OutputType.TIMEML, "/heideltime/config.props", POSTagger.TREETAGGER, false);
		String toProcess = concatSentences(document.getSentences());
		String processed;
		try {
			processed = ht.process(toProcess);
		} catch (DocumentCreationTimeMissingException e) {
			e.printStackTrace();
			return null;
		}
		String[] sentences = processed.split("#SENTENCE#");
		for (int i = 0; i < sentences.length; i++) {
			String sentence = sentences[i];
			List<String> dates = getDates(sentence);
			for (String date : dates) {

				List<String> sentenceList = tweetsByDate.get(date);
				if (sentenceList == null)
					sentenceList = new ArrayList<String>();
				sentenceList.add(trimToTweet(document.getSentences().get(i - 1)));
			//	sentenceList.add(date + ": " + document.getSentences().get(i - 1));
				tweetsByDate.put(date, sentenceList);
			}
		}
		
		System.out.println(tweetsByDate);
		return tweetsByDate;
	}

	private String concatSentences(List<String> sentences) {
		StringBuffer sb = new StringBuffer();
		for (String s : sentences) {
			sb.append("#SENTENCE#" + s);
		}
		return sb.toString();
	}

	private String trimToTweet(String sentence) {
		if (sentence.length() > 140) {
			sentence = sentence.substring(0, 141);
		}
		return sentence;
	}

	private List<String> getDates(String processed) {
		List<String> dates = new ArrayList<String>();
		String dateRegex = "<TIMEX3 tid=\"t[0-9]*\" type=\"DATE\" value=\"(.{2,10})\">";
		String timeRegex = "<TIMEX3 tid=\"t[0-9]*\" type=\"TIME\" value=\"(.{2,50})\">";
		Pattern pattern = Pattern.compile(dateRegex);
		Matcher matcher = pattern.matcher(processed);
		Pattern pattern2 = Pattern.compile(timeRegex);
		Matcher matcher2 = pattern2.matcher(processed);
		String date = null;
		String time = null;
		while (matcher.find()) {
			date = matcher.group(1);
			if (date.contains("-")) {
				dates.add(date);
			}
			while (matcher2.find()) {
				time = matcher2.group(1);
				dates.add(time);
			}
		}
		return dates;
	}

}
