/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Joni Lehtola
 */
@Controller
public class RedirectController {
    @RequestMapping(value = "/redirect", method = RequestMethod.GET, params = "to")
    public String redirect(@RequestParam String to) {
        return "redirect:http://"+to;
    }
}
