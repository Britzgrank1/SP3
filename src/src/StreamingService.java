import util.FileIO;
import util.TextUI;

import java.util.ArrayList;

public class StreamingService {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Media> mediaLibary = new ArrayList<>();
    TextUI ui = new TextUI();
    FileIO io = new FileIO();
    User currentUser;

    public void startSession(){
        ui.displayMsg("Welcome to Chill");
        System.out.println();
        int choice = ui.promptNumeric("Press 1 for login\nPress 2 for create new user.");

        if(choice == 1){
            ui.displayMsg("Going to login screen");
            userLogin();
        } else if (choice == 2){
            ui.displayMsg("Going to create user screen");
            createUser();} //Tror der skal være currentUser.username, currentUser.Password her - Seif
            else if (choice == 0){
                endSession();
            }
            else {
                ui.displayMsg("Invalid choice, try again");
                startSession();
            }
        }

    private void userLogin() {
    }


    public void endSession(){
        ui.displayMsg("Exiting Chill");

    }


    private void createUser(){
        String username = TextUI.promptText("Insert username:"); //String foran username tror jeg burde fjernes -seif
        String password = TextUI.promptText("Insert password:"); //String foran Password tror jeg burde fjernes -seif

        User user = new User(username, password);

        ArrayList<String> userData = new ArrayList<>();
        for (User u: users){
            String s = u.toString();
            userData.add(s);
        }
        io.saveData(userData, "data/userData.csv", "Username, Password");

        System.out.println("New user added " + username);


    }

}
