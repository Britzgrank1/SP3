package model;

import java.util.Arrays;

public class Season {
    private Episode[] episodes = new Episode[0];

    public void addEpisode(Episode e) {
        episodes = Arrays.copyOf(episodes, episodes.length + 1);
        episodes[episodes.length - 1] = e;
    }

    public Episode[] getEpisodes() {
        return episodes;
    }

    @Override
    public String toString() {
        return "model.Season{" +
                ", episodes=" + episodes.length +
                '}';
    }
}
