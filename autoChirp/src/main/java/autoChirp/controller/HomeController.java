package autoChirp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @RequestMapping(value = "/")
	public String Index() {
		return "redirect:/home";
	}

  @RequestMapping(value = "/home")
	public ModelAndView home() {
		return new ModelAndView("home");
	}

  @RequestMapping(value = "/about")
	public ModelAndView about() {
		return new ModelAndView("about");
	}

  @RequestMapping(value = "/contact")
	public ModelAndView contact() {
		return new ModelAndView("contact");
	}

  @RequestMapping(value = "/help")
	public ModelAndView help() {
		return new ModelAndView("help");
	}

  @RequestMapping(value = "/login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}
}
