package service;
import model.StreamingService;
import model.User;
import util.FileIO;
import util.TextUI;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserManager {
    User currentUser;
    ArrayList<User> users = new ArrayList<>();
    TextUI ui = new TextUI();
    FileIO io = new FileIO();
    StreamingService s;

    public UserManager(StreamingService s){
        this.s = s;
    }

    public void userLogin() {
        String username = TextUI.promptText("Insert username: ");
        String password = TextUI.promptText("Insert password: ");
        boolean userExists = false;
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)
                    && u.getPassword().equals(password)) {
                userExists = true;
                currentUser = u;
            }
        }
        if (userExists) {
            System.out.println("Login successful! Welcome " + currentUser.getUsername());
            s.setCurrentUser(currentUser);
            s.userMenu();
        } else {
            System.out.println("Username or password incorrect. Please try again.");
            int tryAgain = ui.promptNumeric("Press 1 to try again\nPress 2 to return to main menu");
            if (tryAgain == 1) {
                userLogin(); //
            } else {
                s.startSession();
            }
        }
    }

    public void createUser() {
        String username = TextUI.promptText("Insert username:");
        String password = TextUI.promptText("Insert password:");
        User user = new User(username, password); // creater nye users
        users.add(user); // adder users
        ArrayList<String> userData = new ArrayList<>(); // ArrayList til at gemme users
        for (User u : users) {
            String s = u.toString();
            userData.add(s);
        }
        io.saveData(userData, "Data/userData.csv", "Username, Password"); // gemmer users til til userData.csv
        System.out.println("New user added: " + username); // printer en besked at brugeren er gemt
    }

    public void loadUsers() {
        ArrayList<String> userData = io.readData("Data/userData.csv");
        users.clear();

        for (String data : userData) {
            String[] info = data.split(",");
            if (info.length >= 2) {
                String username = info[0].trim();
                String password = info[1].trim();
                users.add(new User(username, password));
            }
        }
    }

    public void listAllUsers() {

        ui.displayMsg("There is " + users.size() + " users in the system");
        ui.displayMsg("==============================================");
        for (User u : users) {
            System.out.printf("%25s \n", u.getUsername());
        }
        ui.displayMsg("\n==============================================");

    }
    public void deleteUser() {
        ui.displayMsg("To delete user, you must login to that user");
        String username = TextUI.promptText("Insert username:");
        String password = TextUI.promptText("Insert password:");

        boolean userExists = false;
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)
                    && u.getPassword().equals(password)) {
                userExists = true;
                currentUser = u;
            }
        }
        if (userExists) {
            System.out.println("Delete successful!, " + currentUser.getUsername() + " is now deleted");
            users.remove(currentUser);
            s.deleteUserFiles("Data/"+currentUser.getUsername()+"watchedlist.csv");
            s.deleteUserFiles("Data/"+currentUser.getUsername()+"Favourites.csv");
            saveUsers();

        } else {
            System.out.println("Username or password incorrect. Please try again.");
            int tryAgain = ui.promptNumeric("Press 1 to try again\nPress 2 to return to main menu");
            if (tryAgain == 1) {
                deleteUser();
            } else {
                s.startSession();
            }
        }
    }

    public void saveUsers() {
        try (FileWriter writer = new FileWriter("Data/userData.csv")) {
            writer.write("Username, Password\n");
            for (User u : users) {
                writer.write(u.getUsername() + ", " + u.getPassword());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}