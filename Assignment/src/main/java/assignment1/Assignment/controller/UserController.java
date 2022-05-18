package assignment1.Assignment.controller;

import assignment1.Assignment.entity.User;
import assignment1.Assignment.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;

@Controller
public class UserController {

    // User service init
    @Autowired
    private UserService userService;

    /**
     * Index page
     * @return index page
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Registering new user - GET
     * @param model model attribute
     * @return register page
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Registering new user - POST
     * @param user user object
     * @param bindingResult Validator
     * @param model model attribute
     * @return if unsuccessful=>register page, successful=>login page
     */
    @PostMapping("/register")
    public String registerDB(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registerForm", user);
            return "register";
        }

        //TODO-> Password encryption will be added.
        try{
            userService.saveUser(user);
        } catch (Exception e) {
            System.out.println("Exception caught: " + e);
            return "register";
        }
        return "registerSuccess";
    }

    /**
     * User login - GET
     * @param model model attribute
     */
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "loginPage";
    }

    /**
     * User login - POST
     * @param model model attribute
     * @param user User object for validation
     * @param httpSession session object
     */
    @PostMapping("/login")
    public String loginDB(Model model,
                          @Valid User user,
                          HttpSession httpSession) {
        String verification = userService.verifyUser(user.getEmail(), user.getPassword(), httpSession);
        if (verification.equals("success")) {
            return "loginSuccess";
        }
        //TODO-> Trying to access dashboard only as admin
        else if (verification.equals("success") && httpSession.getAttribute("userEmail").equals("admin@admin.com")) {
            return "redirect:/dashboard";
        }
        else {
            model.addAttribute("verification", verification);
            System.out.println("Verification failed.");

            return "loginPage";
        }

    }

    /**
     * Uploading resume page
     * @param model model attribute
     * @param httpSession session object
     * @return If session users are correct, redirecting to resume upload page. Else returns to login page.
     */
    @GetMapping("/resumeUpload")
    public String resumeUpload(Model model, HttpSession httpSession) {
        if (httpSession.getAttribute("userLoggedIn") != null && httpSession.getAttribute("userLoggedIn").equals("yes")) {
            return "resumeUpload";
        }
        return "unauthorized";
    }

    /**
     * Submitting file and saving it inside static folder, saving the file name in database
     * @param file submitted file
     * @return redirecting to index page
     */
    @PostMapping("/submitResume")
    public String submitResume(@RequestParam("formFile") MultipartFile file, HttpSession httpSession) throws IOException {
        if (httpSession.getAttribute("userLoggedIn") != null && httpSession.getAttribute("userLoggedIn").equals("yes")) {
            String myLocation = System.getProperty("user.dir") + "/src/main/resources/static/";
            String filename = file.getOriginalFilename();

            File mySavedFile = new File(myLocation + filename);

            InputStream inputStream = file.getInputStream();

            OutputStream outputStream = new FileOutputStream(mySavedFile);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            String fileLink = "http://localhost:8080/" + filename;

            User user = new User();
            user.setFileName(fileLink);
            userService.saveUser(user);

            return "uploadSuccess";
        }

        return "unauthorized";
    }

    /**
     * Ending Session
     * @param model model attribute
     */
    @GetMapping("/logout")
    public String logout(Model model, HttpSession httpSession) {
        httpSession.invalidate();
        model.addAttribute("applicationName", "Asset Management");
        return "redirect:/";
    }
}
