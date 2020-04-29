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
            line = "CREATE TABLE appointments (patient_name,doctor_name,apt_date,apt_time,topic): line format /(\\w*);(\\w*);([^ ]*) ([^ ]*);(.*$) /file\"C:\\Users\\Wesley Monk\\Documents\\TestNotes\\test.txt\"";

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
            line = "SELECT date, time, src_ip FROM mylog WHERE date >= '2019-01-01'";
            if (validateCommand(line, selectRegex)) {
                statementWorked = true;
            } else {
                System.out.println("SELECT STATEMENT INVALID. TRY AGAIN");
            }
        } while (!statementWorked);
        selectFromTable(line, table);
    }

    public void selectFromTable(String line, Table table) {
        try {
            FileInputStream in = new FileInputStream("textFiles\\test.txt");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
            System.out.println(jsonString);

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

        for (String val: mt.group("colName").split(",")) {
            colNamesList.add(val);
        }

        return new Table(tableName, colNamesList, colRegex, filePath);
    }

    public boolean validateCommand(String line, String regex) {
        Pattern pt = Pattern.compile(regex);
        Matcher mt = pt.matcher(line);
        if (mt.matches() == true) {
            return true;
        }
        else {
            return false;
        }
    }
}