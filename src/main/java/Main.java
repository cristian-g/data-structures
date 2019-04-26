import controller.InstaSalle;

public class Main {

    public static void main(String[] args) throws Exception {
        boolean close;
        do {
            InstaSalle instasalle = new InstaSalle();
            close = instasalle.init();
        } while(!close);
    }
}