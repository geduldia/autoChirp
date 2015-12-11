package autoChirp;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class WikipediaController {

    @RequestMapping(value="/", method=RequestMethod.GET, params="url")
    public @ResponseBody String test(@RequestParam("url") String url) {
       WikipediaParser parser = new WikipediaParser();
       Document doc = parser.parse(url);
       return "title: "+doc.getTitle()+ "\n"+ "lang: "+doc.getLanguage();
    }

}
