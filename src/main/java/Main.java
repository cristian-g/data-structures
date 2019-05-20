import controller.InstaSalle;
import timetest.TimeTest;

public class Main {
    public static void main(String[] args) {
        TimeTest timeTest = new TimeTest();
        timeTest.runTimeTest2();
        InstaSalle instasalle = new InstaSalle();
        instasalle.init();
    }
}