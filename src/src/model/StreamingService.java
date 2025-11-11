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
    UserManager userManager = new UserManager(this);

    public void setCurrentUser(User user) {
        this.currentUser = user;
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

    public void saveToWatched() {

        try (FileWriter writer = new FileWriter("Data/" + currentUser.getUsername() + "watchedList.csv")) {
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

    public void favouritesList() {
        String title = TextUI.promptText("Type the name of the movie or series to add to favourites:");
        File favouritesFile = new File("Data/" + currentUser.getUsername() + "Favourites.csv");
        try (FileWriter writer = new FileWriter(favouritesFile, true)) {
            if (favouritesFile.length() == 0) {
                writer.write("Favourites\n");
            }
            writer.write(title + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showFavourites() {
        ui.displayMsg("\nFavourite titles for " + currentUser.getUsername() + ":");
        if (currentUser.getFavourites().isEmpty()) {
            ui.displayMsg("You haven't added any favourites yet!");
        } else {
            for (String title : currentUser.getFavourites()) {
                ui.displayMsg("- " + title);
            }
        }
    }

    public void searchMovie() {
        String searchFilm = TextUI.promptText("Type movie name:");
        boolean found = false;

        for (Media m : mediaLibrary) {
            if (m.getTitle().equalsIgnoreCase(searchFilm)) {
                found = true;
                Playable playable = (Playable) m;

                ui.displayMsg("The movie " + m.getTitle() + " has been found!");

                int choice = ui.promptNumeric("Choose and action\n" +
                        "1)Play Moive\n2)Pause Movie\n3)Stop Movie\n4)Go back to menu");

                if (choice == 1) playable.playMovie();
                else if (choice == 2) playable.pauseMovie();
                else if (choice == 3) playable.stopMovie();
                else if (choice == 4) userMenu();

                if (m instanceof Playable) {

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
        } else if (media instanceof Series s) {
            s.playMovie();
            currentUser.addSeen(s.getTitle());
            ui.displayMsg("Saved to watched list: " + s.getTitle());
        } else {
            ui.displayMsg("Unknown media type.");
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

    private void removeFromList(Media media) {
        if (currentUser.getSeen().remove(media.getTitle())) {
            ui.displayMsg(media.getTitle() + " removed from saved list.");
        } else {
            ui.displayMsg(media.getTitle() + " was not in saved list.");
        }
    }

    public void showWatchedList() {
        ui.displayMsg("\nWatched titles for " + currentUser.getUsername() + ":");
        if (currentUser.getSeen().isEmpty()) {
            ui.displayMsg("You haven't watched anything yet!");
        } else {
            for (String title : currentUser.getSeen()) {
                ui.displayMsg("- " + title);
            }
        }
    }

    public void deleteSavedMedia() {
        showWatchedList();
        try (FileWriter writer = new FileWriter("Data/" + currentUser.getUsername() + "watchedList.csv")) {
            writer.write("Watched Titles\n");


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
                    "5) Log out"
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
        public void endSession () {
            ui.displayMsg("Exiting Chill");

        }
    }


