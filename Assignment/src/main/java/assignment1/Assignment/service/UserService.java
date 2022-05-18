package assignment1.Assignment.service;

import assignment1.Assignment.entity.User;
import assignment1.Assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserService {

    // User repository init
    @Autowired
    private UserRepository userRepository;

    /**
     * Saving user to the database
     * @param user user object
     */
    public void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     * Verifying user for login
     *
     * @param email    user email entry
     * @param password user password entry
     * @return first match in database
     */
    public String verifyUser(String email, String password, HttpSession httpSession) {
        List<User> users = userRepository.findByEmail(email);

        if (users.size() == 0){
            return "user not registered";
        }
        if (users.get(0).getPassword().equals(password)){
            httpSession.setAttribute("userLoggedIn", "yes");
            httpSession.setAttribute("userName", users.get(0).getName());
            httpSession.setAttribute("userEmail", users.get(0).getEmail());

            return "success";
        } else {
            return "invalid password";
        }
    }

    /**
     * Returning all users as a list from db
     * @return all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


}
