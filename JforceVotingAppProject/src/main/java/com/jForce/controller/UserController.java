package com.jForce.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.jForce.model.Candidate;
import com.jForce.model.User;
import com.jForce.repository.UserRepository;
import com.jForce.service.CandidateService;
import com.jForce.service.UserService;

@Controller
@RequestMapping(path = "/voting")
public class UserController {

	@Autowired
	private UserService us;

	@Autowired
	private CandidateService cs;

	@Autowired
	private UserRepository ur;

	// HOME PAGE
	@GetMapping("")
	public String home() {
		return "home";
	}

	// REGISTER
	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("user", new User());
		return "registration-form";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute User user) {
		this.us.register(user);
		return "redirect:/voting/login";
	}

	// LOGIN
	@GetMapping("/login")
	public String login() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/voting/candidate/list";
	}

	// ADD VOTE
	@GetMapping("/addVote")
	public String addVote(@RequestParam Integer id, Principal principal, Model model) {
		String username = principal.getName();
		User user = this.us.findByEmail(username);
		if (user.getVoted() == false) {
			Candidate c = this.cs.getById(id);
			c.setVotes(c.getVotes() + 1);
			user.setVoted(true);
			this.cs.update(c, id);
			this.ur.save(user);
			return "redirect:/voting/votedPage";
		}
		if (user.getVoted() == true) {
			return "redirect:/voting/alreadyVoted";
		}

		return "redirect:/voting";
	}

	// ALREADY VOTED PAGE
	@GetMapping("/alreadyVoted")
	public String alreadyVoted() {
		return "alreadyVoted";
	}

	@GetMapping("/votedPage")
	public String votedPage() {
		return "votedPage";
	}

	// LOGOUT
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/login";
	}
}
