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
        ui.promptNumeric("Press 1 for login\nPress 2 for create new user.");

    }


    private void createUser(){
        String username = TextUI.promptText("Insert username:");
        String password = TextUI.promptText("Insert password:");

        User user = new User(currentUser.username, currentUser.password);

        users.add(io.saveData( userData,"data/userData.csv","Username, Password"));

        System.out.println("New user added " + username);

    }

}
