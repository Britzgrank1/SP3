public class Episode extends Season{
    protected int episodeNUmber;
    protected int duration;

    public Episode(String title, int year, String category, String title1, int year1, String category1, int episodeNUmber, int duration) {
        super(title, year, category, title1, year1, category1);
        this.episodeNUmber = episodeNUmber;
        this.duration = duration;
    }

    public int getEpisodeNUmber() {
        return episodeNUmber;
    }

    public void setEpisodeNUmber(int episodeNUmber) {
        this.episodeNUmber = episodeNUmber;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
