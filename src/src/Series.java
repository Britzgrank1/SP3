import java.util.ArrayList;

public class Series implements Media{
    protected String title;
    protected int year;
    protected String category;
    protected ArrayList<Episode> episodes = new ArrayList<>();


    public Series(String title, int year, String category,ArrayList<Episode> episodes ) {
        this.title = title;
        this.year = year;
        this.category = category;
        this.episodes = episodes;

    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public int getYear() {
        return year;
    }


}
