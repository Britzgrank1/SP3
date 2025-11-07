import java.util.Arrays;

public class Season {
    private int seasonNumber;
    private Episode[] episodes = new Episode[0];

    public Season(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public void addEpisode(Episode e) {
        episodes = Arrays.copyOf(episodes, episodes.length + 1);
        episodes[episodes.length - 1] = e;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }


    @Override
    public String toString() {
        return "Season{" +
                "seasonNumber=" + seasonNumber +
                ", episodes=" + episodes.length +
                '}';
    }
}
