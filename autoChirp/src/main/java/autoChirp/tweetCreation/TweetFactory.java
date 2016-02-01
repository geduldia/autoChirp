package autoChirp.tweetCreation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import autoChirp.preProcessing.Document;
import autoChirp.preProcessing.HeidelTimeWrapper;
import autoChirp.preProcessing.SentenceSplitter;
import autoChirp.preProcessing.parser.Parser;
import autoChirp.preProcessing.parser.WikipediaParser;
import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;

/**
 * @author Alena Geduldig
 * 
 *         A class to generate Tweets and TweetGroups from different input-data
 *
 */

public class TweetFactory {

	// is needed for tweetDate creation
	private int currentYear;

	public TweetFactory() {
		currentYear = LocalDateTime.now().getYear();
	}
	
	/**
	 * creates a TweetGroup-Object from the given url. Uses the url as group-title
	 *    1. Creates a Document-object with the given Parser
	 *    2. Splits text into sentences using the SentenceSplitter
	 *    3. Tags dates using HeidelTime
	 *    4. Calculates the tweetDate for each tagged date and initialize Tweet-object
	 *    5. Initializes TweetGroup
	 * @param url
	 * @param parser
	 *        - the appropriate parser for the given url (e.g. WikipediaParser for Wikipedia-urls)
	 * @return a new TweetGroup-object
	 */
	public TweetGroup getTweetsFromUrl(String url, Parser parser){
		return getTweetsFromUrl(url, parser, url);
	}

	/**
	 * creates a TweetGroup-Object from the given url.
	 *    1. Creates a Document-object with the given Parser
	 *    2. Splits text into sentences using the SentenceSplitter
	 *    3. Tags dates using HeidelTime
	 *    4. Calculates the tweetDate for each tagged date and initialize Tweet-object
	 *    5. Initializes TweetGroup
	 * @param url 
	 * @param parser
	 *        - the appropriate parser for the given url (e.g. WikipediaParser for Wikipedia-urls)
	 * @param description
	 *        - description-attribute for the created TweetGroup
	 * @return a new TweetGroup-object
	 */
	public TweetGroup getTweetsFromUrl(String url, Parser parser, String description) {	
		Document doc = createDocument(url, parser);
		String[] processedSentences = tagDatesWithHeideltime(doc);
		List<Tweet> tweets = new ArrayList<Tweet>();
		for (int i = 0; i < processedSentences.length; i++) {
			String sentence = processedSentences[i];
			List<String> origDates = extractDates(sentence);
			Tweet tweet;
			for (String date : origDates) {
				String tweetDate = getTweetDate(date);
				tweet = new Tweet(tweetDate,trimToTweet(doc.getSentences().get(i - 1)));		
			    tweets.add(tweet);			}
		}
		currentYear = LocalDateTime.now().getYear();
		TweetGroup group = new TweetGroup(doc.getTitle(), description);
		group.setTweets(tweets);
		return group;
	}

	/**
	 * @param document
	 * @return a list of date-tagged sentences
	 */
	private String[] tagDatesWithHeideltime(Document document) {
		HeidelTimeWrapper ht = new HeidelTimeWrapper(document.getLanguage(), DocumentType.NARRATIVES, OutputType.TIMEML,
				"/heideltime/config.props", POSTagger.TREETAGGER, false);
		String toProcess = concatSentences(document.getSentences());
		String processed;
		try {
			processed = ht.process(toProcess);
		} catch (DocumentCreationTimeMissingException e) {
			e.printStackTrace();
			return null;
		}
		return processed.split("#SENTENCE#");
	}

	/**
	 * @param url
	 * @param parser
	 * @return
	 */
	private Document createDocument(String url, Parser parser) {
		Document doc = parser.parse(url);
        SentenceSplitter splitter = new SentenceSplitter(doc.getLanguage());
        doc.setSentences(splitter.splitIntoSentences(doc.getText()));
        return doc;
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
			sentence = sentence.substring(0, sentence.lastIndexOf(" ")) + "...";
		}
		return sentence;
	}

	private List<String> extractDates(String processed) {
		List<String> dates = new ArrayList<String>();
		String dateRegex = "type=\"DATE\" value=\"([0-9|XXXX]{4}-[0-9]{2}(-[0-9]{2})?)\">";
		String timeRegex = "type=\"TIME\" value=\"(([0-9]{4}|XXXX)-[0-9]{2}(-[0-9]{2})?)(( [A-Z]{2,4})|(T[0-9]{2}:[0-9]{2}(:[0-9]{2})?))\">";
		Pattern pattern = Pattern.compile(dateRegex);
		Matcher matcher = pattern.matcher(processed);
		Pattern pattern2 = Pattern.compile(timeRegex);
		Matcher matcher2 = pattern2.matcher(processed);
		String date = null;
		String time = null;
		while (matcher.find()) {
			date = matcher.group(1);
			// only date with at least a month
			if (date.contains("-")) {
				dates.add(date);
			}
			while (matcher2.find()) {
				time = matcher2.group(1) + matcher2.group(6).replace("T", " ");
				dates.add(time);
			}
		}
		return dates;
	}

	private String getTweetDate(String origDate) {
		boolean midnight = false;
		if (origDate.contains(" 00:00")) {
			midnight = true;
		}
		LocalDateTime ldtOriginal;
		try{
			DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			ldtOriginal = LocalDateTime.parse(origDate, dtFormatter);
			
		}
		catch (DateTimeParseException e){
			try {
				LocalDate ldOriginal = LocalDate.parse(origDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				ldtOriginal = LocalDateTime.of(ldOriginal, LocalTime.of(12, 0));
			} catch (Exception e2) {
				LocalDate ldOriginal = LocalDate.parse(origDate.concat("-01"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				ldtOriginal = LocalDateTime.of(ldOriginal, LocalTime.of(12, 0));
			}
		
		}
		LocalDateTime ldt = LocalDateTime.of(currentYear, ldtOriginal.getMonth(), ldtOriginal.getDayOfMonth(),
				ldtOriginal.getHour(), ldtOriginal.getMinute());
		LocalDateTime today = LocalDateTime.now();
		if (ldt.isBefore(today)) {
			ldt = LocalDateTime.of(currentYear + 1, ldt.getMonth(), ldt.getDayOfMonth(), ldt.getHour(),
					ldt.getMinute());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String formattedDate = ldt.format(formatter);
		if (!midnight) {
			formattedDate = formattedDate.replace(" 00:00", " 12:00");
		}
		return formattedDate;
	}
}
