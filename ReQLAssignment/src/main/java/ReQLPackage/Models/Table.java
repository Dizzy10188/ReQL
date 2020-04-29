package ReQLPackage.Models;

import java.util.ArrayList;

public class Table {

    private String tableName;
    private ArrayList<String> colNames = new ArrayList<String>();
    private String lineFormat;
    private String filePath;

    public Table(String tableName, ArrayList<String> colNames, String lineFormat, String filePath) {
        this.tableName = tableName;
        this.colNames = colNames;
        this.lineFormat = lineFormat;
        this.filePath = filePath;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ArrayList<String> getColNames() {
        return colNames;
    }

    public void setColNames(ArrayList<String> colNames) {
        this.colNames = colNames;
    }

    public String getLineFormat() {
        return lineFormat;
    }

    public void setLineFormat(String lineFormat) {
        this.lineFormat = lineFormat;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
