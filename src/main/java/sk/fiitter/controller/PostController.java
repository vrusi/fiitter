package sk.fiitter.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sk.fiitter.model.Post;

public class PostController {

    @GetMapping("/users/post")
    public String create(Model model) {
        model.addAttribute("post", new Post());
        return "welcome";
    }
}
