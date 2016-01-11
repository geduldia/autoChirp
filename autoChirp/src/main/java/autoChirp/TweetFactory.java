package autoChirp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;

/**
 * @author geduldia
 * 
 *         A class to generate date-related tweets from a document using
 *         Heideltime as temporal Tagger
 *
 */

public class TweetFactory {

	int currentYear;

	public TweetFactory() {
		currentYear = LocalDateTime.now().getYear();
	}

	/**
	 * @param document
	 * @return tweetsByDate - a map of the detected dates and their including
	 *         sentences trimmed to a tweet-length of 140 characters @throws
	 */
	public Map<String, List<String>> getTweets(Document document) {
		Map<String, List<String>> tweetsByDate = new TreeMap<String, List<String>>();
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
		String[] sentences = processed.split("#SENTENCE#");
		for (int i = 0; i < sentences.length; i++) {
			String sentence = sentences[i];
			System.out.println("sentence: " + sentence);
			List<String> origDates = getDates(sentence);
			for (String string : origDates) {
				System.out.println(string);
			}
			for (String date : origDates) {
				String tweetDate = getTweetDate(date);
				List<String> sentenceList = tweetsByDate.get(tweetDate);
				if (sentenceList == null){
					sentenceList = new ArrayList<String>();
				}		
				sentenceList.add(date + ": " + document.getSentences().get(i - 1));
			    tweetsByDate.put(tweetDate, sentenceList);
			}
		}
		currentYear = LocalDateTime.now().getYear();
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
			sentence = sentence.substring(0, sentence.lastIndexOf(" ")) + "...";
		}
		return sentence;
	}

	private List<String> getDates(String processed) {
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

	public String getTweetDate(String origDate) {
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
		//LocalDateTime ldtOriginal = LocalDateTime.parse(origDate, dtfb.toFormatter());
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
