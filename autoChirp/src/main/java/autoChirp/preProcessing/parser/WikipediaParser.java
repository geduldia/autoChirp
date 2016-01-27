package autoChirp.preProcessing.parser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.cyberneko.html.HTMLConfiguration;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import autoChirp.preProcessing.Document;
import de.unihd.dbs.uima.annotator.heideltime.resources.Language;

//TODO Encoding
/**
 * @author geduldia
 * 
 * A Parser for Wikipedia-Urls
 *
 */
public class WikipediaParser implements Parser {
	
	private StringBuilder builder;
	private boolean hasTitle;
	private String title;
	private DOMParser domParser = new DOMParser();
	/**
	 * regex for footnotes
	 */
	private String  regex = "((\\[[0-9]+\\])+(:[0-9]+)?)";
	
	/* (non-Javadoc)
	 * @see autoChirp.Parser#parse(java.lang.String)
	 */
	@Override
	public Document parse(String url){
		try {
			domParser.setProperty("http://cyberneko.org/html/properties/default-encoding" , "UTF-8");
			domParser.parse(url);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		builder = new StringBuilder();	
		hasTitle = false;
		process(domParser.getDocument().getFirstChild());
		
		return new Document(builder.toString().trim(), url, title, getLanguage(url));	
	}

	/**
	 * returns the language of a wikipedia-article taken from the url
	 * @param url
	 * @return language 
	 * 
	 */
	private Language getLanguage(String url) {
		
		String lang = url.split("://")[1].substring(0,2);
		if(lang.equals("de")){
			return Language.GERMAN;
		}
		else{
			return Language.ENGLISH;
		}
	}

	private void process(Node node) {
		String elementName = node.getNodeName().toLowerCase().trim();
		//takes the content of the first h1-element as title
		if (hasTitle == false && elementName.equals("h1")) {
			title = node.getTextContent().trim();
			hasTitle = true;
		}
		//takes the content of each p-element as text
		if (elementName.equals("p")) {
			String elementContent = node.getTextContent().trim().toLowerCase();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(elementContent);
			while(matcher.find()){
				elementContent = elementContent.replace(matcher.group(1), "");
			}
			if (elementContent.length() > 0) {
				builder.append(elementContent).append("\n\n");
			}
		} 
		Node sibling = node.getNextSibling();
		if (sibling != null) {
			process(sibling);
		}
		Node child = node.getFirstChild();
		if (child != null) {
			process(child);
		}
		if (hasTitle == false) {
			title = "ohne Titel";
		}
	}

}
