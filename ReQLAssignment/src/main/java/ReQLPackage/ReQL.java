package ReQLPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReQL {
    public static void main(String[] args) {
        createTable();
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String line;
//        try {
//            line = br.readLine();
//            while(line != null) {
//                if (line.contains(";")) {
//                    break;
//                }
//                else {
//                    line += "\n" + br.readLine();
//                }
//            }
//            System.out.println("you typed: " + line);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void createTable() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            System.out.println("Please define the table structure");
            System.out.println("EX: CREATE TABLE *TABLE_NAME* (\n" +
                    "COLUMN_NAME_2,\n" +
                    "COLUMN_NAME_1,\n" +
                    "etc...\n" +
                    ")\n" +
                    ": line format ((regex 1);(regex 2);etc...\n" +
                    "file \"path to file with extension\"\n");
            line = br.readLine();
            while(line != null) {
                if (line.contains(";")) {
                    break;
                }
                else {
                    line += "\n" + br.readLine();
                }
            }
            System.out.println("---------------------------");
            System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void selectFromTable() {

    }
}
