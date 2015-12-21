package autoChirp;

import java.util.List;

import de.unihd.dbs.uima.annotator.heideltime.resources.Language;


/**
 * @author geduldia
 * 
 * represents a document created from an url consisting of plain-text, url, title, and language.
 * 
 *
 */

public class Document {
	
	private String text;
	private String url;
	private String title;
	private List<String> sentences;
	private Language language;
	
	/**
	 * @param text
	 * @param url
	 * @param title
	 * @param language
	 */
	public Document(String text, String url, String title, Language language){
		this.text = text;
		this.url = url;
		this.title = title;
		this.language = language;
	}
	
	/**
	 * @return language
	 */
	public Language getLanguage(){
		return language;
	}

	/**
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param sentences  - text splitted into sentences
	 */
	public void setSentences(List<String> sentences){
		this.sentences = sentences;
	}
	
	/**
	 * @return sentences
	 */
	public List<String> getSentences(){
		return sentences;
	}


	
	

}
