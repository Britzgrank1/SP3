package model;

import service.UserManager;
import util.FileIO;
import util.TextUI;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class StreamingService {
    ArrayList<Media> mediaLibrary = new ArrayList<>();
    TextUI ui = new TextUI();
    FileIO io = new FileIO();
    User currentUser;
    UserManager userManager = new UserManager(this);
    public Category category = new Category();

    public void setCurrentUser(User user) {
        this.currentUser = user;
        category.setCurrentUser(user);
    }

    public void startSession() {
        userManager.loadUsers();
        loadAllMedia();
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
            endSession();
            System.exit(0);
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

    public void favouritesList() {
        String title = TextUI.promptText("Type the name of the movie or series to add to favourites:");

        boolean found = false;

        for (Media m : mediaLibrary) {
            if (m.getTitle().equalsIgnoreCase(title)) {
                found = true;
                File favouritesFile = new File("Data/" + currentUser.getUsername() + "Favourites.csv");


                try (FileWriter writer = new FileWriter(favouritesFile, true)) {
                    if (favouritesFile.length() == 0) {
                        writer.write("Favourites\n");
                    }
                    writer.write(title + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (found == false) {
            ui.displayMsg("That title does not exist in the library. Try again.");
        }
    }

    private void showFavourites() {
        ui.displayMsg("\nFavourite titles for " + currentUser.getUsername() + ":");
        if (currentUser.getFavourites().isEmpty()) {
            ui.displayMsg("You haven't added any favourites yet.");
        } else {
            for (String title : currentUser.getFavourites()) {
                ui.displayMsg("- " + title);
            }
        }
    }

    public void searchMovie() {
        String searchTitle = TextUI.promptText("Type movie or series name:");
        boolean found = false;

        for (Media m : mediaLibrary) {
            if (m.getTitle().equalsIgnoreCase(searchTitle)) {
                found = true;

                String type = (m instanceof Movie) ? "movie" : "series";
                ui.displayMsg("The " + type + " \"" + m.getTitle() + "\" has been found!");

                int choice = ui.promptNumeric("1) Play " + type + "\n2) Go back to menu");

                if (choice == 1) {
                    Playable playable = (Playable) m;
                    playable.playMovie();

                    currentUser.addSeen(m.getTitle());
                    watchedList(m);
                    ui.displayMsg("Saved to watched list: " + m.getTitle());

                    boolean isPlaying = true;
                    while (isPlaying) {
                        int control = ui.promptNumeric(
                                "Now playing " + m.getTitle() + "\n" +
                                        "1) Pause\n2) Stop\n3) Continue playing"
                        );
                        if (control == 1) {
                            playable.pauseMovie();
                            int pauseChoice = ui.promptNumeric("Movie/Series Paused - "+m.getTitle()+"\n" +
                            "1) Continue playing\n2) Stop playing"
                            );

                            if(pauseChoice == 1){
                                playable.playMovie();
                            }else if (pauseChoice == 2){
                                playable.stopMovie();
                                isPlaying = false;
                            }

                        } else if (control == 2) {
                            playable.stopMovie();
                            isPlaying = false;
                        } else if (control == 3) {
                            playable.playMovie();
                        } else {
                            ui.displayMsg("Invalid choice.");
                        }
                    }

                    String favChoice = TextUI.promptText("Would you like to add this to favourites? (yes/no)");
                    if (favChoice.equalsIgnoreCase("yes")) {
                        File favouritesFile = new File("Data/" + currentUser.getUsername() + "Favourites.csv");
                        try (FileWriter writer = new FileWriter(favouritesFile, true)) {
                            if (favouritesFile.length() == 0) {
                                writer.write("Favourites\n");
                            }
                            writer.write(m.getTitle() + "\n");
                            ui.displayMsg(m.getTitle() + " added to favourites!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (choice == 2) {
                    userMenu();
                } else {
                    ui.displayMsg("Invalid choice.");
                }
                break;
            }
        }
        if (!found) {
            ui.displayMsg("No movie or series found with that name. Try again!");
            int movieNotFound = ui.promptNumeric("(1 Try again\n(2 Go back to menu");
            if (movieNotFound == 1){
                searchMovie();
            } else if (movieNotFound == 2) {
                userMenu();
            }
        }
    }

    private void loadAllMedia() {
        mediaLibrary.clear();

        ArrayList<String> films = io.readData("Data/film.csv");
        ArrayList<String> series = io.readData("Data/serier.csv");

        // Movies
        for (String f : films) {
            if (f != null && f.length() > 1) {
                String[] parts = f.split(";");
                String title = parts[0];
                mediaLibrary.add(new Movie(title));
            }
        }

        // Series
        for (String s : series) {
            if (s != null && s.length() > 1) {
                String[] parts = s.split(";");
                String title = parts[0];
                mediaLibrary.add(new Series(title));
            }
        }

        ui.displayMsg(mediaLibrary.size() + " movies and series loaded.");
    }

    public void showWatchedList() {
        ui.displayMsg("\nWatched titles for " + currentUser.getUsername() + ":");
        if (currentUser.getSeen().isEmpty()) {
            ui.displayMsg("You haven't watched anything yet.");
        } else {
            for (String title : currentUser.getSeen()) {
                ui.displayMsg("- " + title);
            }
        }
    }

    public void deleteSavedMedia() {
        loadFavourites();
        if (currentUser.getFavourites().isEmpty()) {
            ui.displayMsg("List is empty");
            return;
        }
        ui.displayMsg("\nYour current favourite list");
        ArrayList<String> favourites = currentUser.getFavourites();
        for (int i = 0; i < favourites.size(); i++) {
            ui.displayMsg((i + 1) + ") " + favourites.get(i));
        }

        int choice = ui.promptNumeric("Select a number to delete media from favourites, press 0 to cancel");
        if (choice == 0) {
            ui.displayMsg("Canceled, returning...");
            return;
        }
        if (choice < 1 || choice > favourites.size()) {
            ui.displayMsg("Invalid choice, try again");
            return;
        }
        String removed = favourites.remove(choice - 1);
        ui.displayMsg(removed + " is removed from list");
        String fileName = "Data/" + currentUser.getUsername() + "Favourites.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Favourites\n");
            for (String favourite : favourites) {
                writer.write(favourite + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void userMenu() {
        while (true) {
            int choice = ui.promptNumeric("\nUser Menu for " + currentUser.getUsername() + ":\n" +
                    "1) Search for a movie or series\n" +
                    "2) View watched list\n" +
                    "3) View favourites\n" +
                    "4) Add a favourite manually\n" +
                    "5) Delete saved media\n" +
                    "6) Load Movie from category\n" +
                    "7) Load Series from category\n" +
                    "8) Log out"
            );

            if (choice == 1) {
                searchMovie();
            } else if (choice == 2) {
                loadWatchedList();
                showWatchedList();
            } else if (choice == 3) {
                loadFavourites();
                showFavourites();
            } else if (choice == 4) {
                favouritesList();
            } else if (choice == 5) {
                deleteSavedMedia();
            } else if (choice == 6){
                category.showMoviesByCategory();
            }else if (choice == 7){
                category.showSeriesByCategory();
            }
            else if (choice == 8) {
                ui.displayMsg("Logging out...");
                startSession();
            } else {
                ui.displayMsg("Invalid choice. Try again.");
            }
        }
    }

    public void loadFavourites() {
        String fileName = "Data/" + currentUser.getUsername() + "Favourites.csv";
        File favouritesFile = new File(fileName);

        if (!favouritesFile.exists()) {
            ui.displayMsg("No favourites file found for " + currentUser.getUsername() + ".");
            return;
        }

        ArrayList<String> lines = io.readData(fileName);
        currentUser.getFavourites().clear();

        for (String line : lines) {
            boolean isHeader = line.equalsIgnoreCase("Favourites");
            boolean isEmpty = line.isEmpty();

            if (!isHeader && !isEmpty) {
                currentUser.addFavourite(line.trim());
            }
        }

        ui.displayMsg("Loaded " + currentUser.getFavourites().size() + " favourites for " + currentUser.getUsername() + ".");
    }

    public void loadWatchedList() {
        String fileName = "Data/" + currentUser.getUsername() + "watchedList.csv";
        File watchedFile = new File(fileName);

        if (!watchedFile.exists()) {
            ui.displayMsg("No watched list found for " + currentUser.getUsername() + ".");
            return;
        }
        ArrayList<String> lines = io.readData(fileName);
        currentUser.getSeen().clear();
        for (String line : lines) {
            boolean isHeader = line.equalsIgnoreCase("Watched Titles");
            boolean isEmpty = line.isEmpty();
            if (!isHeader && !isEmpty) {
                currentUser.addSeen(line);
            }
        }
        ui.displayMsg("Loaded " + currentUser.getSeen().size() + " watched titles for " + currentUser.getUsername() + ".");
    }
    public boolean deleteUserFiles(String filePath){
        try {
            Path path = Paths.get(filePath);
            if(!Files.exists(path)) {
                ui.displayMsg("File does not exist");
                return false;
            }
            Files.delete(path);
            return true;
        } catch (IOException e) {
            ui.displayMsg("Deletion failed " + e.getMessage());
            return false;
        }
    }
    public void endSession() {
        ui.displayMsg("Exiting Chill");
    }
}