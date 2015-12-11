package autoChirp;

import java.util.List;

import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

public class Document {
	
	private String text;
	private String url;
	private String title;
	private List<String> sentences;
	private Language language;
	private List<String> dates;
	
	public Document(String text, String url, String title, Language language){
		this.text = text;
		this.url = url;
		this.title = title;
		this.language = language;
	}
	
	public Language getLanguage(){
		return language;
	}

	public String getText() {
		return text;
	}

	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}
	
	public void setSentences(List<String> sentences){
		this.sentences = sentences;
	}
	
	public List<String> getSentences(){
		return sentences;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public List<String> getDates() {
		return dates;
	}
	
	

}
