package autoChirp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;

import de.unihd.dbs.heideltime.standalone.DocumentType;
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
	 * @throws  
	 */
	public Map<String, List<String>> getTweets(Document document)  {
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
			List<String> origDates = getDates(sentence);
			for (String date : origDates) {
				List<String> sentenceList = tweetsByDate.get(date);
				if (sentenceList == null)
					sentenceList = new ArrayList<String>();
			//	sentenceList.add(trimToTweet(document.getSentences().get(i - 1)));
				sentenceList.add(date + ": " + document.getSentences().get(i - 1));
				tweetsByDate.put(getTweetDate(date), sentenceList);
			}
		}
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
			sentence = sentence.substring(0, 138);
			sentence = sentence.substring(0, sentence.lastIndexOf(" "))+"...";
		}
		return sentence;
	}

	private List<String> getDates(String processed) {
		List<String> dates = new ArrayList<String>();
		String dateRegex = "<TIMEX3 tid=\"t[0-9]*\" type=\"DATE\" value=\"([0-9|XXXX]{4}-[0-9]{2}(-[0-9]{2})?)\">";
		String timeRegex = "<TIMEX3 tid=\"t[0-9]*\" type=\"TIME\" value=\"(.{2,50})\">";
		Pattern pattern = Pattern.compile(dateRegex);
		Matcher matcher = pattern.matcher(processed);
		Pattern pattern2 = Pattern.compile(timeRegex);
		Matcher matcher2 = pattern2.matcher(processed);
		String date = null;
		String time = null;
		while (matcher.find()) {
			date = matcher.group(1);
			//only date with at least a month
			if (date.contains("-")) {
				dates.add(date);
			}
			while (matcher2.find()) {
				time = matcher2.group(1).replace("T", " ");
				dates.add(time);
			}
		}
		return dates;
	}
	

	public String getTweetDate(String origDate){
    	String[] acceptedFormats = {"yyyy-MM-dd","yyyy-MM","yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss"};
    	Date date = null;
		try {
			date = DateUtils.parseDate(origDate, acceptedFormats);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("unable to parse date: " + origDate);
			return "1111-11-11 11:11:11";
		}
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
	}
}
