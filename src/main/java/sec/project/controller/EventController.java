/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Event;
import sec.project.domain.Signup;
import sec.project.repository.EventRepository;

/**
 *
 * @author J L
 */
@Controller
@RequestMapping("/events")
public class EventController {
    @Autowired
    EventRepository eventRepo;
    
    @RequestMapping(value="", method = RequestMethod.GET)
    public String listAll(Model model) {
        model.addAttribute("events", eventRepo.findAll());
        return "eventlist";
    }
    
    @RequestMapping(value="", method = RequestMethod.POST)
    public String createNew(@RequestParam String name, 
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date date, 
            @RequestParam(name = "priv", required = false) Boolean priv,
            @RequestParam(name = "homepage", required = false) String homepage)  {
        Event event = new Event();
        event.setName(name);
        event.setDate(date);
        if (priv != null) 
            event.setIsPrivate(priv);
        else
            event.setIsPrivate(false);
        event.setHomepage(homepage);
        eventRepo.save(event);
        return "redirect:/events";
    }
    
    @RequestMapping("/{id}")
    public String getDetails(Model model, @PathVariable Long id) {
        model.addAttribute("signup", new Signup());
        model.addAttribute("event", eventRepo.findOne(id));
        return "event";
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable Long id) {
        eventRepo.delete(id);
        return "redirect:/events";
    }
    
    @RequestMapping(value="/{id}/delete", method = RequestMethod.GET)
    public String deletePath(@PathVariable Long id) {
        eventRepo.delete(id);
        return "redirect:/events";
    }
    
    
}
