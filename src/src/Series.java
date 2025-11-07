import java.util.Arrays;

public class Series extends Media {
    private Season[] seasons = new Season[0];

    public Series(String title, int releaseYear, String category, double rating) {
        super(title, releaseYear, category, rating);
    }

    public void addSeason(Season s) {
        seasons = Arrays.copyOf(seasons, seasons.length + 1);
        seasons[seasons.length - 1] = s;
    }

    @Override
    public String toString() {
        return "Series{" +
                "seasons=" + seasons.length +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                '}';
    }
}
