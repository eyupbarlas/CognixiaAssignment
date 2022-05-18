package assignment1.Assignment.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String password;
    private String email;
    private String fileName;

    /**
     * No args constructor
     */
    public User() {
    }

    /**
     * All args constructor
     * @param id user id
     * @param name username
     * @param password user password
     * @param email user email address
     * @param fileName name of the file that is uploaded
     */
    public User(Long id, String name, String password, String email, String fileName) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.fileName = fileName;
    }

    /**
     * Constructor without id
     * @param name username
     * @param password user password
     * @param email user email address
     * @param fileName name of the file that is uploaded
     */
    public User(String name, String password, String email, String fileName) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.fileName = fileName;
    }
}
