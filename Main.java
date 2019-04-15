package hearthlandsoptimizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    
    public static final String INPUT_FILE_NAME  = "tsvin.txt";
    public static final String OUTPUT_FILE_NAME = "tsvout.txt";
    
    public static void main(String[] args) throws IOException {
                
        Path tsvinPath  = Paths.get(INPUT_FILE_NAME);
        Path tsvoutPath = Paths.get(OUTPUT_FILE_NAME);
        
        List<String> tsvData = Files.readAllLines(tsvinPath);
                
        List<String> output = Building.buildAllAndToString(tsvData);
        
        for(String line : tsvData) {
            System.out.println(line);
        }
                
        Files.write(tsvoutPath, output);
        
    }
    
}
