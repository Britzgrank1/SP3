import util.FileIO;
import util.TextUI;

public class Movie extends Media implements Playable  {

    TextUI ui = new TextUI();

    public Movie(String title, int releaseYear, String category,double rating){
            super(title, releaseYear, category, rating);

        }



        @Override
        public String toString () {
            return "Movie{" +
                    ", title=" + title + '\'' +
                    ", releaseYear=" + releaseYear +
                    ", category=" + category + '\'' +
                    ", rating=" + rating +
                    '}';
    }

    @Override
    public void playMovie() {
        ui.displayMsg("The movie " + title + " is playing");
    }

    @Override
    public void stopMovie() {
        ui.displayMsg("The movie " + title +  " is stopped");

    }

    @Override
    public void pauseMovie() {
        ui.displayMsg(title + " has been paused");
    }
}

