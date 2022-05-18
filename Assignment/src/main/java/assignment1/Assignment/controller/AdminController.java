package assignment1.Assignment.controller;

import assignment1.Assignment.entity.User;
import assignment1.Assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;

import java.util.List;

@Controller
public class AdminController {
    //TODO-> dashboard comes here

    // User service init
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Dashboard page that has data tables with links to resumes
     * @return dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("userLoggedIn") != null && httpSession.getAttribute("userLoggedIn").equals("yes")) {
            List<User> users =  userService.getAllUsers();
            model.addAttribute("users" , users);

            return "dashboard";
        }
       return "unauthorized";
    }

}
