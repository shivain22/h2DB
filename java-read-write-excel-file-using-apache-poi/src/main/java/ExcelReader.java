import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {
    private static final String SAMPLE_XLSX_FILE_PATH = "./Jume 2023 - 20 May 2024 merged file_2.xlsx";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/trail_excel";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "mysql";

    public static void main(String[] args) {
        Workbook workbook = null;
        Connection connection = null;

        try {
            File file = new File(SAMPLE_XLSX_FILE_PATH);
            if (!file.exists() || !file.canRead()) {
                throw new IOException("File not found or cannot be read: " + SAMPLE_XLSX_FILE_PATH);
            }

            workbook = WorkbookFactory.create(file);
            System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets: ");

            // Establishing a connection to MySQL database
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // Iterating over each sheet
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                String tableName = sheet.getSheetName().trim().replaceAll("[^a-zA-Z0-9_]", "_");
                System.out.println("Processing sheet: " + tableName);

                // Creating table in MySQL database using the sheet name and first row as column names
                List<String> colNames = createTable(connection, sheet, tableName);

                // Iterating over rows and inserting data into the MySQL database
                DataFormatter dataFormatter = new DataFormatter();
                Iterator<Row> rowIterator = sheet.rowIterator();

                // Skip the header row
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    List<String> rowValues = new ArrayList<>();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int j=0;
                    while (j<colNames.size() || cellIterator.hasNext()) {
                        Cell cell=null;
                        try {
                            cell = cellIterator.next();
                        }catch (Exception e){
                            System.out.println("Exception in cell iterator for row "+row.getRowNum()+": Cell is Empty for column: "+colNames.get(j));
                        }
                        String cellValue = dataFormatter.formatCellValue(cell);
                        rowValues.add(cellValue);
                        j++;
                    }

                    // Log the number of columns and values
                    System.out.println("Inserting into table: " + tableName);
                    System.out.println("Columns: " + colNames.size() + ", Values: " + rowValues.size());

                    // Ensure the number of values matches the number of columns
                    if (rowValues.size() == colNames.size()) {
                        // Inserting data into MySQL database
                        insertData(connection, colNames, rowValues, tableName);
                    } else {
                        System.err.println("Column count doesn't match value count at row: " + row.getRowNum());
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error accessing the file: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> createTable(Connection connection, Sheet sheet, String tableName) throws SQLException {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("The sheet " + tableName + " has no header row.");
        }

        // Enclose table name in backticks and ensure it is sanitized
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS `");
        createTableSQL.append(tableName).append("` (ID INT AUTO_INCREMENT PRIMARY KEY, ");

        Iterator<Cell> cellIterator = headerRow.cellIterator();
        List<String> colNames = new ArrayList<>();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String columnName = cell.getStringCellValue().replaceAll("[^a-zA-Z0-9_]", "_");
            colNames.add(columnName);
            createTableSQL.append("`").append(columnName).append("` VARCHAR(255), ");
        }

        // Remove the last comma and space
        createTableSQL.setLength(createTableSQL.length() - 2);
        createTableSQL.append(")");

        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL.toString())) {
            preparedStatement.execute();
        }
        return colNames;
    }

    private static void insertData(Connection connection, List<String> colNames, List<String> data, String tableName) throws SQLException {
        // Building the insert SQL dynamically based on the number of columns
        StringBuilder insertSQL = new StringBuilder("INSERT INTO `");
        insertSQL.append(tableName).append("` (");

        for (String colName : colNames) {
            insertSQL.append("`").append(colName).append("`, ");
        }

        // Remove the last comma and space
        insertSQL.setLength(insertSQL.length() - 2);
        insertSQL.append(") VALUES (");

        for (int i = 0; i < data.size(); i++) {
            insertSQL.append("?, ");
        }

        // Remove the last comma and space
        insertSQL.setLength(insertSQL.length() - 2);
        insertSQL.append(")");

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL.toString())) {
            for (int i = 0; i < data.size(); i++) {
                if(data.get(i).equals("")){
                    preparedStatement.setString(i + 1,null);
                }
                else{
                    preparedStatement.setString(i + 1, data.get(i));
                }

            }
            preparedStatement.executeUpdate();
        }
    }
}
