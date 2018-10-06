import java.io.*;
import java.util.ArrayList;
import java.util.List;

class FileWorker {
    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }

    static List<String> read(String fileName) throws FileNotFoundException {
        List<String> lines = new ArrayList<>();
        exists(fileName);

        try {
            try (BufferedReader in = new BufferedReader(new InputStreamReader
                    (new FileInputStream(fileName), "cp1251"))) {
                String line;
                while ((line = in.readLine()) != null)
                    lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}