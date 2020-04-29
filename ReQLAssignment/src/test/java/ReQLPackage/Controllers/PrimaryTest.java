package ReQLPackage.Controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimaryTest {

    public String createRegex = "(?i)CREATE TABLE (?<tableName>\\w*)\\s*\\((?<colName>[\\w*,\\s*]+)\\)\\s*:\\s*(?i)line format\\s*\\/(?<colRegex>[\\(\\W\\w*\\)\\s*]+)\\/\\s*(?i)file\\s*\"(?<filePath>\\D*\\d*\\W*\\w*)\"";
    public String selectRegex = "(?i)select (?<colNames>[\\w*,\\s*]+) (?i)from (?<tableName>\\w*) (?i)where (?<colName>\\w*)\\s*(?<operator>>=|<=|>|<|=)\\s*(?<value>\\S*\\s*)";

    Primary primary = new Primary();

    @Test
    void validateCreateCommand() {
        String command = "CREATE TABLE ....";
        boolean result = primary.validateCommand(command, createRegex);
        assertTrue(result);
    }
}