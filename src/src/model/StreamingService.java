package model;

import service.UserManager;
import util.FileIO;
import util.TextUI;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StreamingService {
    ArrayList<Media> mediaLibrary = new ArrayList<>();
    TextUI ui = new TextUI();
    FileIO io = new FileIO();
    User currentUser;
    UserManager userManager = new UserManager();


    public void startSession() {
        userManager.loadUsers();
        ui.displayMsg("Welcome to Chill");
        System.out.println();
        int choice = ui.promptNumeric("Press 1 for login\nPress 2 for create new user\nPress 3 to list all users\nPress 4 delete user\nPress 0 to exit Chill");

        if (choice == 1) {
            ui.displayMsg("Going to login screen");
            userManager.userLogin();
        } else if (choice == 2) {
            ui.displayMsg("Going to create user screen");
            userManager.createUser();
            ui.displayMsg("Returning...");
            startSession();
        } else if (choice == 3) {
            ui.displayMsg("Retrieving all users");
            userManager.listAllUsers();
            ui.displayMsg("Returning...");
            startSession();
        } else if (choice == 4) {
            userManager.deleteUser();
            ui.displayMsg("Returning...");
            startSession();
        } else if (choice == 0) {

        } else {
            ui.displayMsg("Invalid choice, try again");
            startSession();
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

        try (FileWriter writer = new FileWriter("Data/"+currentUser.getUsername()+"watchedList.csv")) {
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
        ArrayList<String> userData = io.readData("Data/film.csv");
        String searchFilm = TextUI.promptText("Type movie name:");
        boolean found = false;
        for (Media m : mediaLibrary) {
            if (m.getTitle().equalsIgnoreCase(searchFilm)) ;
            found = true;
            ui.displayMsg("The movie " + m.getTitle() + " has being found ");


            if (m instanceof Playable) {
                Playable playable = (Playable) m;
                playable.playMovie();
            } else {
                ui.displayMsg("This movie does not exist..");
            }
            break;
        }
        if (!found) {
            ui.displayMsg("model.Movie not found, Try again!");
            searchMovie();
        }
    }
        private void playMedia (Media media) {
            if (media instanceof Movie m) {
                m.playMovie();
            } else if (media instanceof Series s) {
                ui.displayMsg("Playing series: " + s.getTitle());
                for (Season season : s.getSeasons()) {
                    for (Episode ep : season.getEpisodes()) {
                        ep.playMovie();
                    }
                }
            }
            currentUser.addSeen(media.getTitle());
            saveToWatched();
        }
    public void userMenu (Media media){
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


        public void endSession () {
            ui.displayMsg("Exiting Chill");

        }
    }

