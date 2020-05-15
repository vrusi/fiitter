package sk.fiitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sk.fiitter.auth.SecurityService;
import sk.fiitter.auth.UserService;
import sk.fiitter.auth.UserValidator;
import sk.fiitter.model.User;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

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

        return "redirect:/welcome";
    }

    @GetMapping({"/users/login"})
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }


    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }


    //
    //    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    //    public String showRegistrationForm(WebRequest request, Model model) {
    //        model.addAttribute("user", new UserDto());
    //        //model.addAttribute("user", new User());
    //        return "registration";
    //    }
    //
    //    @RequestMapping(value = "user/registration", method = RequestMethod.POST)
    //    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDto userDto, HttpServletRequest request, Errors errors) {
    //
    //        try {
    //            User registered = userService.registerNewUserAccount(userDao);
    //        } catch (UserAlreadyExistsException e) {
    //            ModelAndView mav = new ModelAndView();
    //            mav.addObject("message", "An account for that username/email already exists.");
    //            return mav;
    //        }
    //
    //        return new ModelAndView("successRegister", "user", userDto);
    //    }
    //
    //    //@RequestMapping(value = "user/create", method = RequestMethod.POST)
    //    //public String userCreate(@Valid @ModelAttribute("user") User user,
    //    //                         BindingResult result,
    //    //                         ModelMap model) {
    //    //    if (result.hasErrors()) {
    //    //        return "error";
    //    //    }
    //    //
    //    //    userRepository.save(user);
    //    //    return "redirect:/home"
    //    //}
}
