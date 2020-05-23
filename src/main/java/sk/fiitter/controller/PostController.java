package sk.fiitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sk.fiitter.PostRepository;
import sk.fiitter.auth.SecurityService;
import sk.fiitter.model.Post;

import java.time.LocalDateTime;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SecurityService securityService;

    /*    @PostMapping("/users")
    public String registration(@ModelAttribute("user") User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        String password = user.getPassword();

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(user);
*/

    @PostMapping("/posts")
    public String create(@ModelAttribute("newPost") Post post, BindingResult bindingResult) {
        var user = securityService.findLoggedInUser();

        if (bindingResult.hasErrors()) {
            return "home";
        }

        post.setDateCreated(LocalDateTime.now());
        post.setUser(user);

        postRepository.save(post);

        return "redirect:/home";
    }
}
