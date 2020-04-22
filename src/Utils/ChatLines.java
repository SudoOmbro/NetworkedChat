package Utils;
import java.util.ArrayList;

public class ChatLines {

    private ArrayList<String> stringList;
    private int maxSize;

    public ChatLines(int maxSize) {
        stringList = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public void add(String string) {
        if (stringList.size() == maxSize)
            stringList.remove(0);
        stringList.add(string);
    }

    public ArrayList<String> getStringList() {
        return stringList;
    }

}
