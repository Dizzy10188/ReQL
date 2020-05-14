package ReQLPackage.Controllers;

import ReQLPackage.Models.Table;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PrimaryTest {
    Primary primary = new Primary();

    @Test
    void ShouldFail_ValidateCreateCommand() {
        String command = "CREATE TABLE ....";
        boolean result = primary.validateCommand(command, primary.createRegex);
        assertFalse(result);
    }

    @Test
    void ShouldPass_ValidateCreateCommand() {
        String command = "CREATE TABLE appointments (patient_name,doctor_name,apt_date,apt_time,topic): line format /([\\w\\s]*);([\\W\\w\\s]*);([^ ]*);([^ ]*);(.*$)/file\"C:\\Users\\Wesley Monk\\Documents\\TestNotes\\test.txt\"";
        boolean result = primary.validateCommand(command, primary.createRegex);
        assertTrue(result);
    }

    @Test
    void ShouldFail_ValidateSelectCommand() {
        String command = "SELECT TABLE ....";
        boolean result = primary.validateCommand(command, primary.selectRegex);
        assertFalse(result);
    }

    @Test
    void ShouldPass_ValidateSelectCommand() {
        String command = "SELECT patient_name, topic, doctor_name FROM appointments WHERE apt_date <= 3/1/2020";
        boolean result = primary.validateCommand(command, primary.selectRegex);
        assertTrue(result);
    }

    @Test
    void ShouldFail_CreateTable() {
        String line = "";
        primary.createTable(line);
    }

    @Test
    void ShouldPass_CreateTable() {
        String line = "CREATE TABLE appointments (patient_name,doctor_name,apt_date,apt_time,topic): line format /([\\w\\s]*);([\\W\\w\\s]*);([^ ]*);([^ ]*);(.*$)/file\"C:\\Users\\Wesley Monk\\Documents\\TestNotes\\test.txt\"";
        Table table = primary.createTable(line);
    }

    @Test
    void ShouldPass_SaveTable() {
        String line = "CREATE TABLE appointments (patient_name,doctor_name,apt_date,apt_time,topic): line format /([\\w\\s]*);([\\W\\w\\s]*);([^ ]*);([^ ]*);(.*$)/file\"C:\\Users\\Wesley Monk\\Documents\\TestNotes\\test.txt\"";
        Table table = primary.createTable(line);
        primary.saveTable(table);
    }

    @Test
    void ShouldFail_StringToDate_DoesNotFollowRegex() {
        String line = "Actual words, not dates";
        primary.stringToDate(line);
    }

    @Test
    void ShouldPass_StringToDate() {
        String line = "3/1/2020";
        Date date = primary.stringToDate(line);
        System.out.println(date);
    }
}