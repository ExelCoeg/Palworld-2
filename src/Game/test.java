package Game;

import java.io.File;

public class test {
    public static void main(String[] args) {
        File dir = new File("src/Saves");
        File[] files = dir.listFiles();
        for (File file : files) {
            System.out.println(file.getName());
        }
        
    }
}
