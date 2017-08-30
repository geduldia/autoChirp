package autoChirp.webController;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import autoChirp.DBConnector;
import autoChirp.tweetCreation.Tweet;
import javassist.NotFoundException;

@Controller
public class FlashcardController {

	private HttpSession session;

	/**
	 * Constructor method, used to autowire and inject the HttpSession object.
	 *
	 * @param session
	 *            Autowired HttpSession object
	 */
	@Inject
	public FlashcardController(HttpSession session) {
		this.session = session;
	}

	/**
	 * RequestMapping to allow previewing flashcards when authenticated.
	 *
	 * @param tweetID
	 *            ID of the Tweet intended to be displayed as flashcard.
	 */
	@RequestMapping(value = "/cardpreview/{tweetID}")
	public void cardpreview(HttpServletResponse response, @PathVariable int tweetID) throws Exception {
		if (session.getAttribute("account") == null) {
			response.sendRedirect("/account");
			response.getOutputStream().close();
			return;
		}

		int userID = Integer.parseInt(((Hashtable<String, String>) session.getAttribute("account")).get("userID"));
		Tweet tweetEntry = DBConnector.getTweetByID(tweetID, userID);
		byte[] img = renderImage(tweetEntry);

		if (img == null) {
			response.sendError(404);
			response.getOutputStream().close();
			return;
		}

		response.setContentType("image/png");
		response.getOutputStream().write(img);
		response.getOutputStream().close();
	}

	/**
	 * RequestMapping to allow Twitter fetching flashcards (unauthenticated,
	 * only if Tweet is published).
	 *
	 * @param tweetID
	 *            ID of the Tweet intended to be displayed as flashcard.
	 */
	@RequestMapping(value = "/flashcard/{tweetID}")
	public void flashcard(HttpServletResponse response, @PathVariable int tweetID) throws Exception {
		Tweet tweetEntry = DBConnector.getTweetByID(tweetID);

		if (!tweetEntry.tweeted) {
			response.sendError(404);
			response.getOutputStream().close();
			return;
		}

		byte[] img = renderImage(tweetEntry);

		if (img == null) {
			response.sendError(404);
			response.getOutputStream().close();
			return;
		}

		response.setContentType("image/png");
		response.getOutputStream().write(img);
		response.getOutputStream().close();
	}

	/**
	 * This method takes a Tweet object and returns a rendered flashcard-image
	 * as byte array.
	 *
	 * @param tweetEntry
	 *            Tweet object intended to be displayed as flashcard.
	 * @return flashcard-image as byte array
	 */
	private byte[] renderImage(Tweet tweetEntry) throws Exception {
		if (tweetEntry == null) {
			throw new NotFoundException("Could not create flashcard, sorry!");
		}

		if (tweetEntry.content.length() <= 140) {
			return null;
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		BufferedImage image = new BufferedImage(900, 600, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphic = image.createGraphics();
		graphic.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		String groupName = DBConnector.getGroupTitle(tweetEntry.groupID);
		boolean onlytext = (tweetEntry.imageUrl == null || tweetEntry.imageUrl.isEmpty());

		// colors
		Color text = new Color(51, 51, 51);
		Color background = new Color(220, 200, 120, 100);

		// background image
		BufferedImage bgimg = ImageIO.read(getClass().getClassLoader().getResource("static/img/paper.jpg"));
		graphic.drawImage(bgimg, 0, 0, null);

		// header box
		graphic.setColor(background);
		graphic.fillRect(30, 30, 840, 60);

		// formated texts
		Float x, y, bound;
		AttributedString attstr;
		LineBreakMeasurer measure;
		AttributedCharacterIterator iter;
		FontRenderContext frc = graphic.getFontRenderContext();

		// header
		String header = groupName + " [" + tweetEntry.tweetDate.substring(0,10) + "]";

		graphic.setColor(text);
		attstr = new AttributedString(header);
		attstr.addAttribute(TextAttribute.FONT, new Font(Font.SANS_SERIF, Font.BOLD, 20));
		iter = attstr.getIterator();
		measure = new LineBreakMeasurer(iter, frc);
		measure.setPosition(iter.getBeginIndex());

		x = 35f;
		y = 68f;
		bound = 830f;

		measure.getPosition();
		measure.nextLayout(bound).draw(graphic, x, y);

		// corpus
		String corpus[] = tweetEntry.content.split("\n");

		y = 120f;
		for (String i : corpus) {
			if (i.isEmpty())
				i = " ";

			graphic.setColor(text);
			attstr = new AttributedString(i.replaceAll("<[^>]*>", ""));
			attstr.addAttribute(TextAttribute.FONT, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
			iter = attstr.getIterator();
			measure = new LineBreakMeasurer(iter, frc);
			measure.setPosition(iter.getBeginIndex());

			x = (onlytext) ? 30 : 360f;
			bound = (onlytext) ? 840 : 510f;
			while (measure.getPosition() < iter.getEndIndex()) {
				TextLayout layout = measure.nextLayout(bound);
				y += layout.getAscent();
				layout.draw(graphic, x, y);
				y += layout.getDescent() + layout.getLeading();
			}
		}

		// image
		if (!onlytext) {

			// image box
			graphic.setColor(background);
			graphic.fillRect(30, 120, 300, 450);

			// image
			BufferedImage img = ImageIO.read(new URL(tweetEntry.imageUrl));
			Integer width = img.getWidth();
			Integer height = img.getHeight();

			if (width > 290) {
				width = 290;
				height = (width * img.getHeight()) / img.getWidth();
			}
			if (height > 420) {
				height = 420;
				width = (height * img.getWidth()) / img.getHeight();
			}

			Integer center = 35 + (290 - width) / 2;
			graphic.drawImage(img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), center, 125, null);
		}

		// done
		ImageIO.write(image, "png", stream);
		return stream.toByteArray();
	}

}
