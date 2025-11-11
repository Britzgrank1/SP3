import util.FileIO;
import util.TextUI;
import java.io.File;
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
        loadUsers();
        loadAllMedia();
        System.out.println();
        int choice = ui.promptNumeric("Press 1 for login\nPress 2 for create new user\nPress 3 to list all users\nPress 4 delete user\nPress 0 to exit Chill");

        if (choice == 1) {
            ui.displayMsg("Going to login screen");
            userLogin();
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
            }
        }


        if (userExists) {
            System.out.println("Login successful! Welcome " + currentUser.getUsername());
            userSession();

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
            String[] info = data.split(",");
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

    public void watchedList(Media media) {
        File WatchedList = new File("Data/" + currentUser.getUsername() + "watchedList.csv");
        try (FileWriter writer = new FileWriter(WatchedList, true)) {
            if (WatchedList.length() == 0) {
                writer.write("Watched Titles\n");
            }
            writer.write(media.getTitle() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToWatched() {

        try (FileWriter writer = new FileWriter("Data/"+currentUser+"watchedList.csv")) {
            writer.write("Watched Titles\n");

            for (String title : currentUser.getSeen()) {
                writer.write(title + "\n");
            }
            ui.displayMsg("Watched list saved for user: " + currentUser.getUsername());
        } catch (IOException e) {
            ui.displayMsg("Error saving watched list: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void searchMovie() {
        String searchFilm = TextUI.promptText("Type movie name:");
        boolean found = false;

        for (Media m : mediaLibrary) {
            if (m.getTitle().equalsIgnoreCase(searchFilm)) {
                found = true;
                ui.displayMsg("The movie " + m.getTitle() + " has been found!");

                if (m instanceof Playable) {
                    Playable playable = (Playable) m;
                    playable.playMovie();

                    currentUser.addSeen(m.getTitle());
                    watchedList(m);
                    ui.displayMsg("Saved to watched list: " + m.getTitle());
                } else {
                    ui.displayMsg("This movie cannot be played.");
                }
                break;
            }
        }
        if (!found) {
            ui.displayMsg("Movie not found, try again!");
            searchMovie();
        }
    }




    private void playMedia(Media media) {
        if (media instanceof Movie m) {
            m.playMovie();
            currentUser.addSeen(m.getTitle());
            ui.displayMsg("Saved to watched list: " + m.getTitle());
        }
        else if (media instanceof Series s) {
            s.playMovie();
            currentUser.addSeen(s.getTitle());
            ui.displayMsg("Saved to watched list: " + s.getTitle());
        }
        else {
            ui.displayMsg("Unknown media type.");
        }
    }


            private void userMenu (Media media){
                ui.displayMsg("You chose: " + media.getTitle());
                ui.displayMsg("1) Play");
                ui.displayMsg("2) Save for later");
                ui.displayMsg("3) Remove from saved list");
                ui.displayMsg("0) Back");

                int choice = ui.promptNumeric("Choose an option: ");
                switch (choice) {
                    case 1 -> playMedia(media);
                    case 2 -> watchedList(media);
                    case 3 -> removeFromList(media);
                    case 0 -> ui.displayMsg("Returning...");
                    default -> ui.displayMsg("Invalid choice");
                }
            }

            private void removeFromList (Media media){
                if (currentUser.getSeen().remove(media.getTitle())) {
                    ui.displayMsg(media.getTitle() + " removed from saved list.");
                } else {
                    ui.displayMsg(media.getTitle() + " was not in saved list.");
                }
        }


    private void loadAllMedia() {
        mediaLibrary.clear();

        ArrayList<String> films = io.readData("Data/film.csv");
        ArrayList<String> series = io.readData("Data/series.csv");

        // Movies
        for (String f : films) {
            if (f.length() > 1) {
                String title = "";
                for (String letter : f.split("")) {
                    if (letter.equals(";")) {
                        break;
                    }
                    title += letter;
                }
                mediaLibrary.add(new Movie(title));
            }
        }

        // Series
        for (String s : series) {
            if (s.length() > 1) {
                String title = "";
                for (String letter : s.split("")) {
                    if (letter.equals(";")) {
                        break;
                    }
                    title += letter;
                }
                mediaLibrary.add(new Series(title));
            }
        }

        ui.displayMsg(mediaLibrary.size() + " movies and series loaded.");
    }


    private void userSession() {
        boolean running = true;

        while (running) {
            int choice = ui.promptNumeric(
                    "\nUser Menu for " + currentUser.getUsername() + ":\n" +
                            "1) Search for a movie or series\n" +
                            "2) View watched list\n" +
                            "3) Log out"
            );

            if (choice == 1) {
                searchMovie();
            } else if (choice == 2) {
                showWatchedList();
            } else if (choice == 3) {
                ui.displayMsg("Logging out...");
                running = false;
                startSession();
            } else {
                ui.displayMsg("Invalid choice. Try again.");
            }
        }
    }

    private void showWatchedList() {
        ui.displayMsg("\nWatched titles for " + currentUser.getUsername() + ":");
        if (currentUser.getSeen().isEmpty()) {
            ui.displayMsg("You haven't watched anything yet!");
        } else {
            for (String title : currentUser.getSeen()) {
                ui.displayMsg("- " + title);
            }
        }
    }



    public void endSession () {
            ui.displayMsg("Exiting Chill");

        }
    }

