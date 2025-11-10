import util.FileIO;
import util.TextUI;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StreamingService {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Media> mediaLibrary = new ArrayList<>();
    TextUI ui = new TextUI();
    FileIO io = new FileIO();
    User currentUser;


    public void startSession(){

        ui.displayMsg("Welcome to Chill");
        System.out.println();
        int choice = ui.promptNumeric("Press 1 for login\nPress 2 for create new user\nPress 3 to list all users\nPress 4 delete user\nPress 0 to exit Chill");


        if(choice == 1){
            ui.displayMsg("Going to login screen");
            userLogin();

            int movieChoice = ui.promptNumeric("Press 1 to look at the category of movies\nPress 2 to look at the category of series");
            if(movieChoice == 1){
                loadMovies();
                showMoviesByCategory();
            }


        } else if (choice == 2){
            ui.displayMsg("Going to create user screen");
            createUser();
            ui.displayMsg("Returning...");
            startSession();
        } else if (choice == 3){
            ui.displayMsg("Retrieving all users");
            listAllUsers();
            ui.displayMsg("Returning...");
            startSession();
        } else if (choice == 4) {
            deleteUser();
            ui.displayMsg("Returning...");
            startSession();
        } else if (choice == 0){

            }
            else {
                ui.displayMsg("Invalid choice, try again");
                startSession();
            }
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
                break;
            }
        }


        if (userExists) {
            System.out.println("Login successful! Welcome " + currentUser.getUsername());

        } else {
            System.out.println("Username or password incorrect. Please try again.");
            int tryAgain = ui.promptNumeric("Press 1 to try again\nPress 2 to return to main menu");
            if (tryAgain == 1) {
                userLogin(); //
            } else {
                startSession();
            }
        }

    }
    public void createUser(){
        String username = TextUI.promptText("Insert username:");
        String password = TextUI.promptText("Insert password:");

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

            for (String data : userData) {
                String[] info = data.split(";");
                if (info.length >= 2) {
                    String username = info[0].trim();
                    String password = info[1].trim();
                    users.add(new User(username, password));
                }
            }
        }
    private void loadseries() {

 for (String category : io.readData("Data/serier.csv")){
     switch (category){
         case Drama:
             ui.promptNumeric("1:Drama");
             break;
         case Crime:


     }

 }

    }

    private void loadMovies() {
        for (String line : io.readData("Data/film.csv")) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(";");
            if (parts.length < 4) continue;

            String title = parts[0].trim();
            int year = Integer.parseInt(parts[1].trim());
            String category = parts[2].trim();
            double rating = Double.parseDouble(parts[3].replace(",", ".").trim());

            mediaLibrary.add(new Movie(title, year, category, rating));
        }
    }
    private void showSeriesByCategory(){



    }
    private void showMoviesByCategory() {
        String category = ui.promptText("What category do you want to see: Comedy/Film-noir/Sci-fi/" +
                "Musical/Mystery/Romance/thriller/Romance/War/" +
                "Crime/Drama/Biography/History/Adventure/Family/Fantasy");
        ArrayList<Media> filtered = new ArrayList<>();

        for (Media m : mediaLibrary)
            if (m.getCategory().toLowerCase().contains(category.toLowerCase()))
                filtered.add(m);

        if (filtered.isEmpty()) {
            ui.displayMsg("No movies found in category: " + category);
            return;
        }

        for (int i = 0; i < filtered.size(); i++)
            System.out.println((i + 1) + ". " + filtered.get(i));

        int choice = ui.promptNumeric("Select a movie (0 to cancel):");
        if (choice > 0 && choice <= filtered.size())
            ui.displayMsg("You're now playing " + filtered.get(choice - 1).getTitle());
        else
            ui.displayMsg("Cancelled.");
    }

    private void listAllUsers() {

        ui.displayMsg("There is " + users.size() + " users in the system");
        ui.displayMsg("==============================================");
        for (User u : users) {
            System.out.printf("%25s \n",u.getUsername());
        }
        ui.displayMsg("\n==============================================");


        }
        private void deleteUser(){
            ui.displayMsg("To delete user, you must login to that user");
            String username = TextUI.promptText("Insert username:");
            String password = TextUI.promptText("Insert password:");

            boolean userExists = false;
            for (User u : users) {
                if (u.getUsername().equalsIgnoreCase(username)
                        && u.getPassword().equals(password)) {
                    userExists = true;
                    currentUser = u;
                    break;
                }
            }
            if (userExists) {
                System.out.println("Delete successful!, " + currentUser.getUsername() + " is now deleted");
                users.remove(currentUser);
                saveUsers();

            } else {
                System.out.println("Username or password incorrect. Please try again.");
                int tryAgain = ui.promptNumeric("Press 1 to try again\nPress 2 to return to main menu");
                if (tryAgain == 1) {
                    deleteUser();
                } else {
                    startSession();
                }
            }
        }
        public void saveUsers(){
            try(FileWriter writer = new FileWriter("Data/userData.csv")) {
                writer.write("Username, Password\n");
                for (User u : users) {
                    writer.write(u.getUsername() + ", " + u.getPassword());
                }
            } catch(IOException e){
                    e.printStackTrace();
                }
            }

        public void searchMovie(){

        }

    public void endSession(){
        ui.displayMsg("Exiting Chill");

    }

}
