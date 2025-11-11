package model;

import util.TextUI;

public class Movie extends Media implements Playable {
    private TextUI ui = new TextUI();

    public Movie(String title) {
        super(title);
    }

    public void playMovie() {
        ui.displayMsg("Now playing movie: " + title);
    }
}
