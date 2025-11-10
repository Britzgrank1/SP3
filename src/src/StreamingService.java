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


    public void startSession() {

        ui.displayMsg("Welcome to Chill");
        System.out.println();
        int choice = ui.promptNumeric("Press 1 for login\nPress 2 for create new user\nPress 3 to list all users\nPress 4 delete user\nPress 0 to exit Chill");


        if (choice == 1) {
            ui.displayMsg("Going to login screen");
            userLogin();

            int movieChoice = ui.promptNumeric("Press 1 to look at the category of movies\nPress 2 to look at the category of series");
            if (movieChoice == 1) {

                showMoviesByCategory();


            }


        } else if (choice == 2) {
            ui.displayMsg("Going to create user screen");
            createUser();
            ui.displayMsg("Returning...");
            startSession();
        } else if (choice == 3) {
            ui.displayMsg("Retrieving all users");
            listAllUsers();
            ui.displayMsg("Returning...");
            startSession();
        } else if (choice == 4) {
            deleteUser();
            ui.displayMsg("Returning...");
            startSession();
        } else if (choice == 0) {

        } else {
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
            String[] info = data.split(";");
            if (info.length >= 2) {
                String username = info[0].trim();
                String password = info[1].trim();
                users.add(new User(username, password));
            }
        }
    }


    private void listAllUsers() {

        ui.displayMsg("There is " + users.size() + " users in the system");
        ui.displayMsg("==============================================");
        for (User u : users) {
            System.out.printf("%25s \n", u.getUsername());
        }
        ui.displayMsg("\n==============================================");


    }

    private void deleteUser() {
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

    public void searchMovie() {

    }

    public void endSession() {
        ui.displayMsg("Exiting Chill");

    }

    private void showMoviesByCategory() {
        String selectedCategory = "repeat";
        while (selectedCategory == "repeat") {
            ui.displayMsg("Select a category:");
            ui.displayMsg("1. Comedy");
            ui.displayMsg("2. Drama");
            ui.displayMsg("3. Action");
            ui.displayMsg("4. Sci-fi");
            ui.displayMsg("5. Romance");
            ui.displayMsg("6. Thriller");
            ui.displayMsg("7. Horror");
            ui.displayMsg("8. Biography");
            ui.displayMsg("9. Family");
            ui.displayMsg("10. Adventure");
            ui.displayMsg("0. Cancel");

            int choice = ui.promptNumeric("Enter your choice:");

            switch (choice) {
                case 1:
                    selectedCategory = "Comedy";
                    break;
                case 2:
                    selectedCategory = "Drama";
                    break;
                case 3:
                    selectedCategory = "Action";
                    break;
                case 4:
                    selectedCategory = "Sci-fi";
                    break;
                case 5:
                    selectedCategory = "Romance";
                    break;
                case 6:
                    selectedCategory = "Thriller";
                    break;
                case 7:
                    selectedCategory = "Horror";
                    break;
                case 8:
                    selectedCategory = "Biography";
                    break;
                case 9:
                    selectedCategory = "Family";
                    break;
                case 10:
                    selectedCategory = "Adventure";
                    break;
                case 0:
                    ui.displayMsg("Cancelled.");
                    return;
                default:
                    ui.displayMsg("Invalid choice. Please try again.");
                    selectedCategory = "repeat";
            }
        }

        // Read CSV and display matching movies
        ArrayList<String> lines = io.readData("Data/film.csv");
        boolean found = false;

        for (String line : lines) {
            if (line.contains(selectedCategory)) {
                ui.displayMsg(line);
                found = true;
            }
        }

        if (!found) {
            ui.displayMsg("No movies found in category: " + selectedCategory);
        }
    }

}
