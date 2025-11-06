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
            login();
        } else if (choice == 2){
            ui.displayMsg("Going to create user screen");
            createUser();
            else if (choice == 0){
                endSession();
            }
            else {
                ui.displayMsg("Invalid choice, try again");
                startSession();
            }
        }
    }



    public void endSession(){
        ui.displayMsg("Exiting Chill");

    }


    private void createUser(String username, String password){
        String username = TextUI.promptText("Insert username:");
        String password = TextUI.promptText("Insert password:");

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
