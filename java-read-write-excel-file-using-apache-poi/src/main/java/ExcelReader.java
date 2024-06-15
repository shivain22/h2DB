
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ExcelReader {
    private static final String SAMPLE_XLSX_FILE_PATH = "./sample-xlsx-file.xlsx";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/trail_excel"; // or jdbc:h2:mem:test for in-memory database
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "mysql";

    public static void main(String[] args) {
        Workbook workbook = null;
        Connection connection = null;

        try {
            workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

            System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

            // Establishing a connection to H2 database
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            // Iterating over each sheet
            Iterator<Sheet> sheetIterator = workbook.sheetIterator();
            while (sheetIterator.hasNext()) {
                Sheet sheet = sheetIterator.next();
                String tableName = sheet.getSheetName();
                System.out.println("Processing sheet: " + tableName);

                // Creating table in H2 database using the sheet name and first row as column names
                List<String> colNames=createTable(connection, sheet, tableName);

                // Iterating over rows and inserting data into the H2 database
                DataFormatter dataFormatter = new DataFormatter();
                Iterator<Row> rowIterator = sheet.rowIterator();

                // Skip the header row
                if (rowIterator.hasNext()) {
                    rowIterator.next();
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    StringBuilder rowValues = new StringBuilder();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        String cellValue = dataFormatter.formatCellValue(cell);
                        rowValues.append(cellValue).append(",");
                    }

                    // Inserting data into H2 database
                    insertData(connection, colNames, rowValues.toString().split(","), tableName);
                }
            }

        } catch (IOException | SQLException e) {
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

        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createTableSQL.append(tableName).append(" (ID INT AUTO_INCREMENT PRIMARY KEY, ");

        Iterator<Cell> cellIterator = headerRow.cellIterator();
        List<String> colNames= new ArrayList<>();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String columnName = cell.getStringCellValue();
            String modifiedColName=String.join("_", columnName.split(" "));
            colNames.add(modifiedColName);
            createTableSQL.append(modifiedColName).append(" VARCHAR(255), ");
        }

        // Remove the last comma and space
        createTableSQL.setLength(createTableSQL.length() - 2);
        createTableSQL.append(")");

        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL.toString())) {
            preparedStatement.execute();
        }
        return colNames;
    }

    private static void insertData(Connection connection,List<String> colNames, String[] data, String tableName) throws SQLException {
        // Building the insert SQL dynamically based on the number of columns
        StringBuilder insertSQL = new StringBuilder("INSERT INTO ");
        insertSQL.append(tableName).append(" (");

        for (int i = 0; i < data.length; i++) {
            insertSQL.append(colNames.get(i)).append(", ");
        }

        // Remove the last comma and space
        insertSQL.setLength(insertSQL.length() - 2);
        insertSQL.append(") VALUES (");

        for (int i = 0; i < data.length; i++) {
            insertSQL.append("?, ");
        }

        // Remove the last comma and space
        insertSQL.setLength(insertSQL.length() - 2);
        insertSQL.append(")");

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL.toString())) {
            for (int i = 0; i < data.length; i++) {
                preparedStatement.setString(i + 1, data[i]);
            }
            preparedStatement.executeUpdate();
        }
    }

}

