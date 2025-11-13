package model;

import util.TextUI;

public class Episode implements Playable {
    private String title;
    private int episodeNumber;
    private int duration; // i minutter
    TextUI ui = new TextUI();

    public Episode(String title, int episodeNumber, int duration) {
        this.title = title;
        this.episodeNumber = episodeNumber;
        this.duration = duration;
    }

    public String getTitle() { return title; }
    public int getEpisodeNumber() { return episodeNumber; }
    public int getDuration() { return duration; }

    @Override
    public void playMovie() {
        ui.displayMsg("Playing " + title + " episode "+ episodeNumber );
    }

    @Override
    public void pauseMovie() {
        ui.displayMsg("Pausing episode: " + title + episodeNumber);
    }

    @Override
    public void stopMovie() {
        ui.displayMsg(title + episodeNumber + " is Stopped:");
    }

    @Override
    public String toString() {
        return "model.Episode{" +
                "title='" + title + '\'' +
                ", episodeNumber=" + episodeNumber +
                ", duration=" + duration +
                '}';
    }
}