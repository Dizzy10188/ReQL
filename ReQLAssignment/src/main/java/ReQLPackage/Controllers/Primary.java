package ReQLPackage.Controllers;

import ReQLPackage.Models.Table;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        String line = "";
        System.out.println("Please define the table structure \n*names can only be a single line, no spaces* *All text must be on a single line*");
        System.out.println("EX: CREATE TABLE *TABLE_NAME* (COLUMN_NAME_1, COLUMN_NAME_2): line format ((regex 1),(regex 2)etc...)file \"path to file with extension\"");
        System.out.println("---------------------------\n");
        do {
//            line = "CREATE TABLE appointments (patient_name,doctor_name,apt_date,apt_time,topic): line format /([\\w\\s]*);([\\W\\w\\s]*);([^ ]*);([^ ]*);(.*$)/file\"C:\\Users\\Wesley Monk\\Documents\\TestNotes\\test.txt\"";
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
//            line = "SELECT patient_name, topic, doctor_name FROM appointments WHERE apt_date <= 3/1/2020";
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            System.out.println(line);
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

        String format = "|";
        String formattedString = "";

        for (int i = 0; i < colNamesList.size(); i++) {
            format += " %-20s |";
        }

        if (colNamesList.size() == 1) {
            formattedString = String.format(format, colNamesList.get(0));
        } else if (colNamesList.size() == 2) {
            formattedString = String.format(format, colNamesList.get(0), colNamesList.get(1));
        } else if (colNamesList.size() == 3) {
            formattedString = String.format(format, colNamesList.get(0), colNamesList.get(1), colNamesList.get(2));
        } else if (colNamesList.size() == 4) {
            formattedString = String.format(format, colNamesList.get(0), colNamesList.get(1), colNamesList.get(2), colNamesList.get(3));
        } else if (colNamesList.size() == 5) {
            formattedString = String.format(format, colNamesList.get(0), colNamesList.get(1), colNamesList.get(2), colNamesList.get(3), colNamesList.get(4));
        } else {
            System.out.println("I didn't account for this");
        }

        System.out.println(formattedString);
        System.out.println("--------------------------------------------------------------------------------------------------------------------");

        String fileString = "";

        try {
            FileReader fr = new FileReader(table.getFilePath());
            BufferedReader br = new BufferedReader(fr);

            while ((fileString = br.readLine()) != null) {
                Pattern pat = Pattern.compile(table.getLineFormat());
                Matcher mat = pat.matcher(fileString);
                mat.find();

                if (table.getColNames().contains(colName)) {
                    int colGroup = table.getColNames().indexOf(colName) + 1;
                    Date date = stringToDate(mat.group(colGroup));
                    Date selectDate = stringToDate(value);
                    String stringFormat = "";
                    String formatAmount = "|";
                    ArrayList<String> colResults = new ArrayList<String>();

                    switch (operator) {
                        case ">=":
                            if (date.after(selectDate) || date.equals(selectDate)) {
                                for (int i = 0; i < colNamesList.size(); i++) {
                                    formatAmount += " %-20s |";
                                }

                                if (colNamesList.size() == 1)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0));
                                }
                                else if (colNamesList.size() == 2)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1));
                                }
                                else if (colNamesList.size() == 3)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2));
                                }
                                else if (colNamesList.size() == 4)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(3)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2), colResults.get(3));
                                }
                                else if (colNamesList.size() == 5)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(3)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(4)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2), colResults.get(3), colResults.get(4));
                                }
                                else
                                {
                                    System.out.println("I didn't account for this");
                                }
                                System.out.println(stringFormat);
                            }
                            break;
                        case "<=":
                            if (date.before(selectDate) || date.equals(selectDate)) {
                                for (int i = 0; i < colNamesList.size(); i++) {
                                    formatAmount += " %-20s |";
                                }

                                if (colNamesList.size() == 1)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0));
                                }
                                else if (colNamesList.size() == 2)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1));
                                }
                                else if (colNamesList.size() == 3)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2));
                                }
                                else if (colNamesList.size() == 4)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(3)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2), colResults.get(3));
                                }
                                else if (colNamesList.size() == 5)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(3)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(4)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2), colResults.get(3), colResults.get(4));
                                }
                                else
                                {
                                    System.out.println("I didn't account for this");
                                }
                                System.out.println(stringFormat);
                            }
                            break;
                        case ">":
                            if (date.after(selectDate)) {
                                for (int i = 0; i < colNamesList.size(); i++) {
                                    formatAmount += " %-20s |";
                                }

                                if (colNamesList.size() == 1)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0));
                                }
                                else if (colNamesList.size() == 2)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1));
                                }
                                else if (colNamesList.size() == 3)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2));
                                }
                                else if (colNamesList.size() == 4)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(3)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2), colResults.get(3));
                                }
                                else if (colNamesList.size() == 5)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(3)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(4)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2), colResults.get(3), colResults.get(4));
                                }
                                else
                                {
                                    System.out.println("I didn't account for this");
                                }
                                System.out.println(stringFormat);
                            }
                            break;
                        case "<":
                            if (date.before(selectDate)) {
                                for (int i = 0; i < colNamesList.size(); i++) {
                                    formatAmount += " %-20s |";
                                }

                                if (colNamesList.size() == 1)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0));
                                }
                                else if (colNamesList.size() == 2)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1));
                                }
                                else if (colNamesList.size() == 3)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2));
                                }
                                else if (colNamesList.size() == 4)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(3)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2), colResults.get(3));
                                }
                                else if (colNamesList.size() == 5)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(3)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(4)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2), colResults.get(3), colResults.get(4));
                                }
                                else
                                {
                                    System.out.println("I didn't account for this");
                                }
                                System.out.println(stringFormat);
                            }
                            break;
                        case "=":
                            if (date.equals(selectDate)) {
                                for (int i = 0; i < colNamesList.size(); i++) {
                                    formatAmount += " %-20s |";
                                }

                                if (colNamesList.size() == 1)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0));
                                }
                                else if (colNamesList.size() == 2)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1));
                                }
                                else if (colNamesList.size() == 3)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2));
                                }
                                else if (colNamesList.size() == 4)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(3)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2), colResults.get(3));
                                }
                                else if (colNamesList.size() == 5)
                                {
                                    int x = table.getColNames().indexOf(colNamesList.get(0)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(1)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(2)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(3)) + 1;
                                    colResults.add(mat.group(x));
                                    x = table.getColNames().indexOf(colNamesList.get(4)) + 1;
                                    colResults.add(mat.group(x));
                                    stringFormat = String.format(formatAmount, colResults.get(0), colResults.get(1), colResults.get(2), colResults.get(3), colResults.get(4));
                                }
                                else
                                {
                                    System.out.println("I didn't account for this");
                                }
                                System.out.println(stringFormat);
                            }
                            break;
                        default:
                            System.out.println("The operator given was invalid");
                    }
                } else {
                    System.out.println("The column name specified in the WHERE clause is Invalid");
                }
            }

            br.close();
            fr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        line = line.trim();
        Pattern pt = Pattern.compile(createRegex);
        Matcher mt = pt.matcher(line);
        mt.find();
        if (!line.isEmpty()) {
            String tableName = mt.group("tableName");
            String colRegex = mt.group("colRegex");
            String filePath = mt.group("filePath");

            ArrayList<String> colNamesList = new ArrayList<String>();

            for (String val : mt.group("colName").split(",")) {
                colNamesList.add(val);
            }

            return new Table(tableName, colNamesList, colRegex, filePath);
        } else {
            throw new IllegalArgumentException("Line is blank. Please fix it");
        }
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

    public Date stringToDate(String dob) {
        String dateRegex = "(((0?[1-9]|1[012])/(0?[1-9]|1\\d|2[0-8])|(0?[13456789]|1[012])/(29|30)|(0?[13578]|1[02])/31)/(19|[2-9]\\d)\\d{2}|0?2/29/((19|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|(([2468][048]|[3579][26])00)))";
        Pattern pt = Pattern.compile(dateRegex);
        Matcher mt = pt.matcher(dob);
        if (mt.matches() == true) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = null;
            try {
                date = formatter.parse(dob);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        } else {
            System.out.println("The date given is invalid");
            return null;
        }
    }

}
