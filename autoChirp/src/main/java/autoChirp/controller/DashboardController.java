package autoChirp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

  @RequestMapping(value = "/dashboard")
	public ModelAndView dashboard() {
    ModelAndView mv = new ModelAndView("dashboard");

    String test = "teststring";

    mv.addObject("test", test);

    return mv;
	}
}
