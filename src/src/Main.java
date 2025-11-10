public class Main {
    public static void main(String[] args) {
        StreamingService s = new StreamingService();
        s.loadUsers();
        s.startSession();
        s.endSession();
        s.searchMovie();


    }
}
