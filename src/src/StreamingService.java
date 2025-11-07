import util.FileIO;
import util.TextUI;

import java.util.ArrayList;

public class StreamingService {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Media> mediaLibrary = new ArrayList<>();
    TextUI ui = new TextUI();
    FileIO io = new FileIO();
    User currentUser;

    public void startSession(){

        int choice = ui.promptNumeric("Press 1 for login\nPress 2 for create new user\nPress 0 to exit Chill");

        if(choice == 1){
            ui.displayMsg("Going to login screen");
            userLogin();
        } else if (choice == 2) {
            ui.displayMsg("Going to create user screen");
            createUser(); //Tror der skal være currentUser.username, currentUser.Password her - Seif
            ui.displayMsg("Returning...");
            startSession();
        }    else if (choice == 0){

            }
            else {
                ui.displayMsg("Invalid choice, try again");
                startSession();
            }
        }

    private void userLogin() {
        String username = TextUI.promptText("Insert username: ");
        String password = TextUI.promptText("Insert password: ");

        boolean found = false;
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                found = true;
                currentUser = u;
                break;
            }
        }

        // Reager på resultatet
        if (found) {
            System.out.println("Login successful! Welcome " + currentUser.getUsername());
            // Her kan du fx gå videre til mainMenu()
        } else {
            System.out.println("Username or password incorrect. Please try again.");
            int retry = ui.promptNumeric("Press 1 to try again\nPress 2 to return to main menu");
            if (retry == 1) {
                userLogin(); // prøv igen
            } else {
                startSession(); // tilbage til hovedmenu
            }
        }

    }


    public void endSession(){
        ui.displayMsg("Exiting Chill");

    }

    private void createUser(){
        loadUsers();
        String username = TextUI.promptText("Insert username:"); //String foran username tror jeg burde fjernes -seif
        String password = TextUI.promptText("Insert password:"); //String foran Password tror jeg burde fjernes -seif

        User user = new User(username, password); // creater nye users
        users.add(user); // adder users

        ArrayList<String> userData = new ArrayList<>(); // ArrayList til at gemme users
        for (User u: users){
            String s = u.toString();
            userData.add(s);
        }
        io.saveData(userData, "Data/userData.csv", "Username, Password"); // gemmer users til til userData.csv

        System.out.println("New user added: " + username); // printer en besked at brugeren er gemt
    }

    public void loadUsers() {
        ArrayList<String> userData = io.readData("Data/userData.csv");
        users.clear();
        for (String data: userData){
            String[] info = data.split(",");
            if(info.length>= 2){
                String username = info[0].trim();
                String password  = info[1].trim();
                users.add(new User(username, password));
            }
        }
    }
}
