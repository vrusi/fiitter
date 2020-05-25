package sk.fiitter.controller;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.fiitter.PostRepository;
import sk.fiitter.auth.SecurityService;
import sk.fiitter.auth.UserRepository;
import sk.fiitter.auth.UserService;
import sk.fiitter.auth.UserValidator;
import sk.fiitter.model.Post;
import sk.fiitter.model.User;


import java.io.File;
import java.io.IOException;
import java.util.Base64;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/users/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/users")
    public String registration(@ModelAttribute("user") User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        String password = user.getPassword();

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(user);

        securityService.autoLogin(user.getUsername(), password);

        return "redirect:/";
    }

    @GetMapping({"/login"})
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping("/")
    public String home(Model model) {
        var user = securityService.findLoggedInUser();
        model.addAttribute("currentUser", user);
        model.addAttribute("posts", postRepository.getHomeFeedByUser(user));
        model.addAttribute("newPost", new Post());
        return "index";
    }

    @GetMapping(value = "/profiles/{username}")
    public String getUserBySimplePathWithPathVariable(
            @PathVariable("username") String username, Model model) throws IOException {

        var user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        boolean isFollowedAlready = user.getFollowers().stream().anyMatch(follower -> follower.getUsername().equals(securityService.findLoggedInUsername()));
        String action = isFollowedAlready ? "Unfollow" : "Follow";
        model.addAttribute("action", action);

        model.addAttribute("currentUser", securityService.findLoggedInUser());

        // QRCode generation
        File file = QRCode.from("www.fiitter.sk/profiles/" + username).to(ImageType.PNG)
                .withSize(200, 200)
                .file();

        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        model.addAttribute("qrcodecontent", encodedString);
        return "profile";
    }

    @PostMapping(value = "/profiles/{username}/follow")
    public String follow(@PathVariable("username") String username) {
        var user = userRepository.findByUsername(username);
        user.getFollowers().add(securityService.findLoggedInUser());
        userRepository.save(user);
        return "redirect:/profiles/" + username;
    }

    @PostMapping(value = "/profiles/{username}/unfollow")
    public String unfollow(@PathVariable("username") String username) {
        var user = userRepository.findByUsername(username);
        user.getFollowers().remove(securityService.findLoggedInUser());
        userRepository.save(user);
        return "redirect:/profiles/" + username;
    }

}
