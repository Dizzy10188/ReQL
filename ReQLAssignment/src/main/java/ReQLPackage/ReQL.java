package ReQLPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReQL {
    public static String createRegex = "[Cc][Rr][Ee][Aa][Tt][Ee] [Tt][Aa][Bb][Ll][Ee] \\w* \\([\\w*;\\s*]+\\)\\s*:\\s*[Ll][Ii][Nn][Ee]\\s*[Ff][Oo][Rr][Mm][Aa][Tt]\\s*\\([\\(\\W\\w*\\)\\s*]+\\)\\s*[Ff][Ii][Ll][Ee]\\s*\"\\D*\\d*\\W*\\w*\"";
    public static void main(String[] args) {
        createTable();
    }

    public static void createTable() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
//        try {
            System.out.println("Please define the table structure \n*names can only be a single line, no spaces* *All text must be on a single line*");
            System.out.println("EX: CREATE TABLE *TABLE_NAME* (COLUMN_NAME_1; COLUMN_NAME_2;): line format ((regex 1),(regex 2)etc...)file \"path to file with extension\"");
            System.out.println("---------------------------\n");
//            line = br.readLine();
            line = "CREATE TABLE Products (Price;Amount;): line format ((\\w*)(\\w*))file\"C:\\Users\\Wesley Monk\\Documents\\TestNotes\\test.txt\"";

            Pattern pt = Pattern.compile(createRegex);
            Matcher mt = pt.matcher(line);
            if (mt.matches() == true) {
                System.out.println("Worked\n");
                System.out.println(line);
            }
            else {
                System.out.println("Didn't work");
            }

//            while(line != null) {
//                if (line.contains(";")) {
//                    break;
//                }
//                else {
//                    line += "\n" + br.readLine();
//                }
//            }

            System.out.println("---------------------------");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void selectFromTable() {

    }
}
