package main.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import main.repo.User;
import main.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * Controller for get- and post - requests from user
 *
 *
 */

@Controller
public class UserController {

    // the application context is injected via ctor
    private ApplicationContext context;

    @Autowired
    public UserController(ApplicationContext c) {
        this.context = c;
    }

    private UserRepository getRepo() {
        return (UserRepository)this.context.getBean(UserRepository.class);
    }

    @GetMapping("/")
    public String main(User user, Model model) {
        return "session";
    }

    @GetMapping("/readme")
    public String readme_string() {
        return "readme";
    }
//    @GetMapping("/index")
//    public String indexstring(User user, Model model) {
//        return "index";
//    }

    @GetMapping("/adduser")
    public String main(User user, Model model,HttpSession session) {
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

        if (messages == null) {
            messages = new ArrayList<>();
        }
        model.addAttribute("sessionMessages", messages);
        return "add-user";
    }


    @GetMapping("/signup")
    public String showSignUpForm(User user) {
        return "add-user";
    }

    @GetMapping("/search")
    public String searchMessage() {
        return "search_message";
    }

    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model,HttpSession session) {
        if (result.hasErrors()) {
            return "add-user";
        }
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");
        String message = messages.get(0);
        user.setName(message);
        getRepo().save(user);
        model.addAttribute("users", getRepo().findAll());
        return "add-user";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {

        User user = getRepo().findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }

        getRepo().save(user);
        model.addAttribute("users", getRepo().findAll());
        return "index";
    }

    @GetMapping("/delete/{id}")
        public String deleteUser(@PathVariable("id") long id, Model model) {

            User user = getRepo().findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
            getRepo().delete(user);
            model.addAttribute("users", getRepo().findAll());
            return "index";
        }

        @GetMapping(value="/json")
        public String json (Model model) {
            return "json";
        }
        /**
         * a sample controller return the content of the DB in JSON format
         * @param model
         * @return
         */
        @GetMapping(value="/getjson")
        public @ResponseBody List<User> getAll(Model model) {

            return getRepo().findAll();
    }
}

