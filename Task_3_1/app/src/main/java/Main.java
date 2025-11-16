import java.io.IOException;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        String fileName = "file.txt";
        String substring = "Привет";

        Finder finder = new Finder();

        List<Long> indices = finder.find(fileName, substring);

        System.out.println(indices);

        System.out.println("finish");
    }
}
