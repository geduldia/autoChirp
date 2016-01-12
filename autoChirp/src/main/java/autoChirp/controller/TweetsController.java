package autoChirp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TweetsController {

  @RequestMapping(value = "/tweets")
	public ModelAndView tweets() {
		return new ModelAndView("tweets");
	}

  @RequestMapping(value = "/groups")
	public ModelAndView home() {
		return new ModelAndView("groups");
	}

}
