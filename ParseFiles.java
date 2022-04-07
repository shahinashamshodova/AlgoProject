package AlgoProject;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files


public class ParseFiles{

    public static final int MAX_VERTICES = 10000;

    public static Graph parseFiles(){

        Graph g = new Graph(MAX_VERTICES);

        try {
            File myObj = new File("stop_times.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return
    }

    public static void main(String[] args) {}
}
