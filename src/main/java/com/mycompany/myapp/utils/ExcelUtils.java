package com.mycompany.myapp.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtils {

    public static String getCellValue(Sheet sheet, int row, int col) {
        // Lấy ô hiện tại
        Row currentRow = sheet.getRow(row);
        if (currentRow != null) {
            Cell cell = currentRow.getCell(col);
            if (cell != null && !isPartOfMergedRegion(sheet, row, col)) {
                return getCellValueAsString(cell);
            }
        }
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(row, col)) {
                Row mergedRow = sheet.getRow(mergedRegion.getFirstRow());
                if (mergedRow != null) {
                    Cell mergedCell = mergedRow.getCell(mergedRegion.getFirstColumn());
                    return getCellValueAsString(mergedCell);
                }
            }
        }
        return "";
    }

    private static boolean isPartOfMergedRegion(Sheet sheet, int row, int col) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(row, col)) {
                return true;
            }
        }
        return false;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
