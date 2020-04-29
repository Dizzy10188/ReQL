package ReQLPackage.Controllers;

import ReQLPackage.Models.Table;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Primary {
    public String createRegex = "(?i)CREATE TABLE (?<tableName>\\w*)\\s*\\((?<colName>[\\w*,\\s*]+)\\)\\s*:\\s*(?i)line format\\s*\\/(?<colRegex>[\\(\\W\\w*\\)\\s*]+)\\/\\s*(?i)file\\s*\"(?<filePath>\\D*\\d*\\W*\\w*)\"";
    public String selectRegex = "(?i)select (?<colNames>[\\w*,\\s*]+) (?i)from (?<tableName>\\w*) (?i)where (?<colName>\\w*)\\s*(?<operator>>=|<=|>|<|=)\\s*(?<value>\\S*\\s*)";

    public static void main(String[] args) {
        Primary primary = new Primary();
        primary.run();
    }

    public void run() {
        boolean statementWorked = false;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        System.out.println("Please define the table structure \n*names can only be a single line, no spaces* *All text must be on a single line*");
        System.out.println("EX: CREATE TABLE *TABLE_NAME* (COLUMN_NAME_1, COLUMN_NAME_2): line format ((regex 1),(regex 2)etc...)file \"path to file with extension\"");
        System.out.println("---------------------------\n");
        do {
            line = "CREATE TABLE appointments (patient_name,doctor_name,apt_date,apt_time,topic): line format /([\\w\\s]*);([\\W\\w\\s]*);([^ ]*) ([^ ]*);(.*$) /file\"C:\\Users\\Wesley Monk\\Documents\\TestNotes\\test.txt\"";

//            line = "CrEatE TABLE Products\n" +
//                    "(Price,Amount): \n" +
//                    "line format /(\\w*)(\\w*)/\n" +
//                    "file\"C:\\Users\\Wesley Monk\\Documents\\TestNotes\\test.txt\"";
            if (validateCommand(line, createRegex)) {
                statementWorked = true;
            } else {
                System.out.println("CREATE STATEMENT INVALID. TRY AGAIN");
            }
        } while (!statementWorked);
        Table table = createTable(line);
        saveTable(table);


        System.out.println("Write a Select Statement");
        System.out.println("EX: SELECT Col_Name_1, Col_Name_1, etc.. FROM Col_Name WHERE Col_Name >= Value");
        System.out.println("---------------------------\n");
        statementWorked = false;
        do {
            line = "SELECT patient_name, topic, doctor_name FROM appointments WHERE apt_date = '3/1/2020'";
            if (validateCommand(line, selectRegex)) {
                statementWorked = true;
            } else {
                System.out.println("SELECT STATEMENT INVALID. TRY AGAIN");
            }
        } while (!statementWorked);
        selectFromTableAndPrint(line, table);
    }

    public void selectFromTableAndPrint(String line, Table table) {
        Pattern pt = Pattern.compile(selectRegex);
        Matcher mt = pt.matcher(line);
        mt.find();
        ArrayList<String> colNamesList = new ArrayList<String>();
        String tableName = mt.group("tableName");
        String colName = mt.group("colName");
        String operator = mt.group("operator");
        String value = mt.group("value");

        for (String val : mt.group("colNames").split(",")) {
            colNamesList.add(val.trim());
        }

        String fileString = "";

        try {
            FileReader fr = new FileReader(table.getFilePath());
            BufferedReader br = new BufferedReader(fr);

//            System.out.println(table.getLineFormat());

            while ((fileString = br.readLine()) != null) {
//                System.out.println(fileString);
                Pattern pat = Pattern.compile(table.getLineFormat());
                Matcher mat = pat.matcher(fileString);
                mat.find();
                for (String val : colNamesList) {
                    int x = table.getColNames().indexOf(val) + 1;
                    String y = mt.group(x);
                    System.out.println(y);
                }


                table.getLineFormat();
            }

            br.close();
            fr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        System.out.println("----------------------");
//        System.out.println(table.getColNames());
//        System.out.println("----------------------");
//        System.out.println(colNamesList);
//        System.out.println(tableName);
//        System.out.println(colName);
//        System.out.println(operator);
//        System.out.println(value);


//        try {
//            FileInputStream in = new FileInputStream(table.getFilePath());
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    public void saveTable(Table table) {
        try {
            // create a new OutputStreamWriter
            OutputStream os = new FileOutputStream("textFiles\\test.txt");
            OutputStreamWriter writer = new OutputStreamWriter(os);

            // create a new FileInputStream to read what we write
            FileInputStream in = new FileInputStream("textFiles\\test.txt");

            //Creating the ObjectMapper object
            ObjectMapper mapper = new ObjectMapper();
            //Converting the Object to JSONString
            String jsonString = mapper.writeValueAsString(table);
//            System.out.println(jsonString);

            // write something in the file
            writer.write(jsonString);

            // flush the stream
            writer.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Table createTable(String line) {
        Pattern pt = Pattern.compile(createRegex);
        Matcher mt = pt.matcher(line);
        mt.find();
        String tableName = mt.group("tableName");
        String colRegex = mt.group("colRegex");
        String filePath = mt.group("filePath");

        ArrayList<String> colNamesList = new ArrayList<String>();

        for (String val : mt.group("colName").split(",")) {
            colNamesList.add(val);
        }

        return new Table(tableName, colNamesList, colRegex, filePath);
    }

    public boolean validateCommand(String line, String regex) {
        Pattern pt = Pattern.compile(regex);
        Matcher mt = pt.matcher(line);
        if (mt.matches() == true) {
            return true;
        } else {
            return false;
        }
    }
}
