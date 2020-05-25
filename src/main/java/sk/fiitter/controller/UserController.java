package sk.fiitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
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


import sk.fiitter.QRGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.awt.image.BufferedImage;



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

    @GetMapping({"/home"})
    public String welcomeHome(){
        return "home";
    }

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
            @PathVariable("username") String username, Model model) {

        var user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        boolean isFollowedAlready = user.getFollowers().stream().anyMatch(follower -> follower.getUsername().equals(securityService.findLoggedInUsername()));
        String action = isFollowedAlready ? "Unfollow" : "Follow";
        model.addAttribute("action", action);

        model.addAttribute("currentUser", securityService.findLoggedInUser());
        return "profile";
    }

    @PostMapping(value = "/profiles/{username}/follow")
    public String follow(@PathVariable("username") String username){
        var user = userRepository.findByUsername(username);
        user.getFollowers().add(securityService.findLoggedInUser());
        userRepository.save(user);
        return "redirect:/profiles/" + username;
    }


    @PostMapping(value = "/profiles/{username}/unfollow")
    public String unfollow(@PathVariable("username") String username){
        var user = userRepository.findByUsername(username);
        user.getFollowers().remove(securityService.findLoggedInUser());
        userRepository.save(user);
        return "redirect:/profiles/" + username;
    }

    // QRCode implementation, replace value with data (i think) | src = https://www.baeldung.com/java-generating-barcodes-qr-codes
    @PostMapping(value = "/zxing/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> zxingQRCode(@RequestBody String barcode) throws Exception {
        return okResponse(QRGenerator.generateQRCodeImage(barcode));
    }

    // Helper function for QRCode implementation
    private ResponseEntity<BufferedImage> okResponse(BufferedImage image) {
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    // Helper function for QRCode implementation
    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

}
