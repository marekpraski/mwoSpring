package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class SchoolsController {

    @RequestMapping(value="/Schools")
    public String listSchools(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
    	
        return "schoolsList";    
    }
    
    @RequestMapping(value="/AddSchool")
    public String displayAddSchoolForm(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
        return "schoolForm";    
    }   

    @RequestMapping(value="/EditSchool")
    public String displayEditSchoolForm(@RequestParam(value="schoolId", required=false) String schoolId, Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	model.addAttribute("schoolToEdit", DatabaseConnector.getInstance().getSchoolToEdit(schoolId));   	
        return "schoolEditForm";    
    }

    @RequestMapping(value="/CreateSchool", method=RequestMethod.POST)
    public String createSchool(@RequestParam(value="schoolName", required=false) String name,
    		@RequestParam(value="schoolAddress", required=false) String address,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	School school = new School();
    	school.setName(name);
    	school.setAddress(address);
    	
    	DatabaseConnector.getInstance().addSchool(school);    	
       	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
    	model.addAttribute("message", "Nowa szkoła została dodana");
         	
    	return "schoolsList";
    }
    
    @RequestMapping(value="/SaveEditedSchool", method=RequestMethod.POST)
    public String SaveEditedSchool(@RequestParam(value="schoolId", required=false) String schoolId, @RequestParam(value="schoolName", required=false) String name,
    		@RequestParam(value="adr", required=false) String address, Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	School schoolToEdit = DatabaseConnector.getInstance().getSchoolToEdit(schoolId);
    	schoolToEdit.setAddress(address);
    	schoolToEdit.setName(name);
    	DatabaseConnector.getInstance().updateSchool(schoolToEdit);
    	  
        model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
    	model.addAttribute("message", "Dane szkoły zostały zmienione");
    	return "schoolsList";
    }
    
    @RequestMapping(value="/DeleteSchool", method=RequestMethod.POST)
    public String deleteSchool(@RequestParam(value="schoolId", required=false) String schoolId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	DatabaseConnector.getInstance().deleteSchool(schoolId);    	
       	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
    	model.addAttribute("message", "Szkoła została usunięta");
         	
    	return "schoolsList";
    }

}