package autoChirp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.unihd.dbs.heideltime.standalone.DocumentType;
import de.unihd.dbs.heideltime.standalone.HeidelTimeStandalone;
import de.unihd.dbs.heideltime.standalone.OutputType;
import de.unihd.dbs.heideltime.standalone.POSTagger;
import de.unihd.dbs.heideltime.standalone.exceptions.DocumentCreationTimeMissingException;
import de.unihd.dbs.uima.types.heideltime.Timex3;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

public class TweetFactory {
	



//	public Map<String,List<String>> detectDates(Document doc) throws DocumentCreationTimeMissingException, SAXException, IOException {
//		Map<String, List<String>> sentencesByDate = new HashMap<String, List<String>>();
//		HeidelTimeStandalone ht = new HeidelTimeStandalone(doc.getLanguage(), DocumentType.NARRATIVES,
//				OutputType.TIMEML, "config.props", POSTagger.TREETAGGER, false);
//		int i = 0;
//		
//		for (String sentence : doc.getSentences()) {
//			if(i > 30) break;
//			i++;
//			String processed = ht.process(sentence);
//			List<String> dates = getDates(processed);
//		
//			for (String date : dates) {
//				
//				List<String> sentenceList = sentencesByDate.get(date);
//				if(sentenceList == null) sentenceList = new ArrayList<String>();
//				sentenceList.add(trimToTweet(sentence));
//				sentencesByDate.put(date, sentenceList);
//			}
//		}
//		
//		return sentencesByDate;
//	}
	
	public Map<String, List<String>> detectDates(Document doc) throws DocumentCreationTimeMissingException {
		Map<String, List<String>> sentencesByDate = new HashMap<String, List<String>>();
		HeidelTimeStandalone ht = new HeidelTimeStandalone(doc.getLanguage(), DocumentType.NARRATIVES,
				OutputType.TIMEML, "config.props", POSTagger.TREETAGGER, false);
		String toProcess = concatSentences(doc.getSentences());
		String processed = ht.process(toProcess);
		String[] sentences = processed.split("#SENTENCE#");
		for (int i = 0; i < sentences.length;i++) {
			String sentence = sentences[i];
			List<String> dates = getDates(sentence);
			for (String date : dates) {
			
			List<String> sentenceList = sentencesByDate.get(date);
			if(sentenceList == null) sentenceList = new ArrayList<String>();
			sentenceList.add(toTweet(date+": "+doc.getSentences().get(i-1)));
			sentencesByDate.put(date, sentenceList);
		}
		}
		return sentencesByDate;
	}


	
	private String concatSentences(List<String> sentences) {
		StringBuffer sb = new StringBuffer();
		for (String s : sentences) {
			sb.append("#SENTENCE#"+s);
		}
		return sb.toString();
	}

	private String toTweet(String sentence) {
		if(sentence.length() > 140){
			sentence = sentence.substring(0,141);
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
