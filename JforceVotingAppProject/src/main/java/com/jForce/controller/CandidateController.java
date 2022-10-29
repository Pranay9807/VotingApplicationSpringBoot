package com.jForce.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jForce.model.Candidate;
import com.jForce.service.CandidateService;

@Controller
@RequestMapping(path = "/voting")
public class CandidateController {

	@Autowired
	private CandidateService cs;

	// ADD
	@GetMapping("/candidate/add")
	public String addForm(Model model) {
		model.addAttribute("candidate", new Candidate());
		return "addform";
	}

	@PostMapping("/candidate/add")
	public String addSubmit(@ModelAttribute Candidate candidate) {
		this.cs.add(candidate);
		return "redirect:/voting/candidate/list";
	}

	// GET ALL
	@GetMapping("/candidate/list")
	public String showAll(Model model) {
		model.addAttribute("candidates", this.cs.getAll());
		return "list";
	}

	// UPDATE
	@GetMapping("/candidate/update/{id}")
	public String update(Model model, @PathVariable("id") Integer id) {
		Candidate candidate = this.cs.getById(id);
		model.addAttribute("candidate", candidate);
		return "updateForm";
	}

	@PostMapping("/candidate/update/{id}")
	public String update(@Valid Candidate candidate, @PathVariable("id") Integer id, Model model) {
		model.addAttribute("candidate", this.cs.update(candidate, id));
		return "redirect:/voting/candidate/list";
	}

	// DELETE
	@GetMapping("/candidate/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		this.cs.deleteById(id);
		return "redirect:/voting/candidate/list";
	}
}
