package autoChirp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("twitterAccount")
public class HomeController {

@RequestMapping(value = "/")
public String Index() {
        return "redirect:/home";
}

@RequestMapping(value = "/home")
public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home");
        return mv;
}

@RequestMapping(value = "/about")
public ModelAndView about() {
        ModelAndView mv = new ModelAndView("about");
        return mv;
}

@RequestMapping(value = "/contact")
public ModelAndView contact() {
        ModelAndView mv = new ModelAndView("contact");
        return mv;
}

@RequestMapping(value = "/help")
public ModelAndView help() {
        ModelAndView mv = new ModelAndView("help");
        return mv;
}

}
