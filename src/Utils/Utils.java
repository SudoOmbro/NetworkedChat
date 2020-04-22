package Utils;

public class Utils {

    public static void say(String ...msgs) {
        for (String msg : msgs)
            System.out.println(Thread.currentThread().getName() + ": " + msg);
    }

}
