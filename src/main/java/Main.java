import controller.InstaSalle;

public class Main {

    public static void main(String[] args) throws Exception {
        boolean close;
        InstaSalle instasalle = new InstaSalle();
        do {
            close = instasalle.init();
        } while(!close);
    }
}