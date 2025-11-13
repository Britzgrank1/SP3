package model;

import util.FileIO;
import util.TextUI;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Category {

    private final TextUI ui = new TextUI();
    private final FileIO io = new FileIO();
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private void saveToWatched(Media media) {
        File watchedFile = new File("Data/" + currentUser.getUsername() + "watchedList.csv");
        try (FileWriter writer = new FileWriter(watchedFile, true)) {
            if (watchedFile.length() == 0) {
                writer.write("Watched Titles\n");
            }
            writer.write(media.getTitle() + "\n");
            ui.displayMsg("Saved to watched list: " + media.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFavourites(Media media) {
        String favChoice = TextUI.promptText("Would you like to add this to favourites? (yes/no)");
        if (favChoice.equalsIgnoreCase("yes")) {
            File favouritesFile = new File("Data/" + currentUser.getUsername() + "Favourites.csv");
            try (FileWriter writer = new FileWriter(favouritesFile, true)) {
                if (favouritesFile.length() == 0) {
                    writer.write("Favourites\n");
                }
                writer.write(media.getTitle() + "\n");
                ui.displayMsg(media.getTitle() + " added to favourites!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void playWithControls(Playable playable, Media media) {
        playable.playMovie();
        saveToWatched(media);
        boolean isPlaying = true;

        while (isPlaying) {
            int control = ui.promptNumeric(
                    "Now playing " + media.getTitle() + "\n" +
                            "1) Pause\n2) Stop\n3) Continue playing"
            );

            if (control == 1) {
                playable.pauseMovie();
                int pauseChoice = ui.promptNumeric(
                        "Paused - " + media.getTitle() + "\n" +
                                "1) Continue playing\n2) Stop playing"
                );

                if (pauseChoice == 1) {
                    playable.playMovie();
                } else if (pauseChoice == 2) {
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

        saveToFavourites(media);
    }

    public void showSeriesByCategory() {
        String selectedCategory = "repeat";

        while (selectedCategory.equals("repeat")) {
            ui.displayMsg(
                    "Select a category:\n" +
                            "1. Comedy\n2. Drama\n3. Action\n4. Sci-fi\n5. Romance\n6. Thriller\n" +
                            "7. Horror\n8. Biography\n9. Family\n10. Adventure\n11. Talk-show\n12. Mystery\n13. Crime\n14. Fantasy\n" +
                            "15. Sport\n16. History\n17. War\n18. Documentary\n0. Cancel"
            );

            int choice = ui.promptNumeric("Enter your choice:");

            switch (choice) {
                case 1 -> selectedCategory = "Comedy";
                case 2 -> selectedCategory = "Drama";
                case 3 -> selectedCategory = "Action";
                case 4 -> selectedCategory = "Sci-fi";
                case 5 -> selectedCategory = "Romance";
                case 6 -> selectedCategory = "Thriller";
                case 7 -> selectedCategory = "Horror";
                case 8 -> selectedCategory = "Biography";
                case 9 -> selectedCategory = "Family";
                case 10 -> selectedCategory = "Adventure";
                case 11 -> selectedCategory = "Talk-show";
                case 12 -> selectedCategory = "Mystery";
                case 13 -> selectedCategory = "Crime";
                case 14 -> selectedCategory = "Fantasy";
                case 15 -> selectedCategory = "Sport";
                case 16 -> selectedCategory = "History";
                case 17 -> selectedCategory = "War";
                case 18 -> selectedCategory = "Documentary";
                case 0 -> {
                    ui.displayMsg("Cancelled.");
                    return;
                }
                default -> {
                    ui.displayMsg("Invalid choice. Please try again.");
                    selectedCategory = "repeat";
                }
            }
        }

        ArrayList<String> lines = io.readData("Data/serier.csv");
        ArrayList<Series> foundSeries = new ArrayList<>();

        for (String line : lines) {
            if (line.contains(selectedCategory)) {
                String[] info = line.split(";");
                if (info.length >= 4) {
                    String title = info[0];
                    foundSeries.add(new Series(title));
                }
            }
        }

        if (foundSeries.isEmpty()) {
            ui.displayMsg("No series found in category: " + selectedCategory);
            return;
        }

        ui.displayMsg("Series in " + selectedCategory + " category:");
        for (int i = 0; i < foundSeries.size(); i++) {
            ui.displayMsg((i + 1) + ". " + foundSeries.get(i).getTitle());
        }

        int choice = ui.promptNumeric("Choose a series to play (0 to cancel):");
        if (choice > 0 && choice <= foundSeries.size()) {
            Series selected = foundSeries.get(choice - 1);
            ui.displayMsg("You're now watching " + selected.getTitle());
            playWithControls(selected, selected);
        } else {
            ui.displayMsg("Cancelled.");
        }
    }

    public void showMoviesByCategory() {
        String selectedCategory = "repeat";

        while (selectedCategory.equals("repeat")) {
            ui.displayMsg(
                    "Select a category:\n" +
                            "1. Comedy\n2. Drama\n3. Action\n4. Sci-fi\n5. Romance\n6. Thriller\n" +
                            "7. Horror\n8. Biography\n9. Family\n10. Adventure\n0. Cancel"
            );

            int choice = ui.promptNumeric("Enter your choice:");

            switch (choice) {
                case 1 -> selectedCategory = "Comedy";
                case 2 -> selectedCategory = "Drama";
                case 3 -> selectedCategory = "Action";
                case 4 -> selectedCategory = "Sci-fi";
                case 5 -> selectedCategory = "Romance";
                case 6 -> selectedCategory = "Thriller";
                case 7 -> selectedCategory = "Horror";
                case 8 -> selectedCategory = "Biography";
                case 9 -> selectedCategory = "Family";
                case 10 -> selectedCategory = "Adventure";
                case 0 -> {
                    ui.displayMsg("Cancelled.");
                    return;
                }
                default -> {
                    ui.displayMsg("Invalid choice. Please try again.");
                    selectedCategory = "repeat";
                }
            }
        }

        ArrayList<String> lines = io.readData("Data/film.csv");
        ArrayList<Movie> foundMovies = new ArrayList<>();

        for (String line : lines) {
            if (line.contains(selectedCategory)) {
                String[] info = line.split(";");
                if (info.length >= 4) {
                    String title = info[0];
                    foundMovies.add(new Movie(title));
                }
            }
        }

        if (foundMovies.isEmpty()) {
            ui.displayMsg("No movies found in category: " + selectedCategory);
            return;
        }

        ui.displayMsg("Movies in " + selectedCategory + " category:");
        for (int i = 0; i < foundMovies.size(); i++) {
            ui.displayMsg((i + 1) + ". " + foundMovies.get(i).getTitle());
        }

        int choice = ui.promptNumeric("Choose a movie to play (0 to cancel):");
        if (choice > 0 && choice <= foundMovies.size()) {
            Movie selected = foundMovies.get(choice - 1);
            ui.displayMsg("You're now watching " + selected.getTitle());
            playWithControls(selected, selected);
        } else {
            ui.displayMsg("Cancelled.");
        }
    }
}
