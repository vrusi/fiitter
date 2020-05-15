package sk.fiitter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import sk.fiitter.dto.UserDto;
import sk.fiitter.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegistrationController {

    //@Autowired
    //private UserRepository userRepository;

    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        model.addAttribute("user", new UserDto());
        //model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value="user/registration", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDto userDto, HttpServletRequest request, Errors errors) {

        try {
            User registered = userService.registerNewUserAccount(userDao);
        } catch (UserAlreadyExistsException e) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }

        return new ModelAndView("successRegister", "user", userDto);
    }

    //@RequestMapping(value = "user/create", method = RequestMethod.POST)
    //public String userCreate(@Valid @ModelAttribute("user") User user,
    //                         BindingResult result,
    //                         ModelMap model) {
    //    if (result.hasErrors()) {
    //        return "error";
    //    }
    //
    //    userRepository.save(user);
    //    return "redirect:/home"
    //}
}
