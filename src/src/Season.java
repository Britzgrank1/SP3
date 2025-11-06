import java.util.ArrayList;

public class Season extends Series{
    protected String title;
    protected int year;
    protected String category;
    protected ArrayList<Episode> episodes = new ArrayList<>();

    public Season(String title, int year, String category, String title1, int year1, String category1,ArrayList <Episode> episodes ) {
        super(title, year, category,episodes);
        this.title = title1;
        this.year = year1;
        this.category = category1;
        this.episodes = episodes;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }
}
