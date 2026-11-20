package wordguesser.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import wordguesser.model.User;

public class UserStore {
    private static final String DATA_DIR = "data";
    private static final String USER_FILE = DATA_DIR + File.separator + "users.txt";

    public UserStore() {
        ensureDataFiles();
    }

    private void ensureDataFiles() {
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }

            Path userPath = Paths.get(USER_FILE);
            if (!Files.exists(userPath)) {
                Files.createFile(userPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize user storage.", e);
        }
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromFileString(line.trim());
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load users.", e);
        }

        return users;
    }

    public User findUser(String username) {
        for (User user : loadUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean registerUser(String username, String password) {
        if (username == null || password == null) return false;

        username = username.trim().toUpperCase();
        password = password.trim();

        if (username.isEmpty() || password.isEmpty()) return false;
        if (findUser(username) != null) return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(new User(username, password).toFileString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public User authenticate(String username, String password) {
        User user = findUser(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void updateUser(User updatedUser) {
        List<User> users = loadUsers();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(updatedUser.getUsername())) {
                    writer.write(updatedUser.toFileString());
                } else {
                    writer.write(user.toFileString());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to update user.", e);
        }
    }
}