package ReQLPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReQL {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            line = br.readLine();
            while(line != null) {
                if (line.contains(";")) {
                    break;
                }
                else {
                    line += "\n" + br.readLine();
                }
            }
            System.out.println("you typed: " + line);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
