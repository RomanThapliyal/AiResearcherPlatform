package service;

import dao.UserDAO;
import model.User;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    public User login(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.isEmpty()) {
            return null;
        }

        User user = userDAO.getUserByEmail(email.trim());

        if (user == null) {
            return null;
        }

        if (user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    public boolean register(User user) {

        if (user == null) {
            return false;
        }

        String name = user.getName();
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole();

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        if (password == null || password.isEmpty()) {
            return false;
        }

        if (role == null || role.trim().isEmpty()) {
            return false;
        }

        if (!role.equals("ADMIN") && !role.equals("RESEARCHER")) {
            return false;
        }

        User existingUser = userDAO.getUserByEmail(email.trim());

        if (existingUser != null) {
            return false;
        }

        return userDAO.addUser(user);
    }
}