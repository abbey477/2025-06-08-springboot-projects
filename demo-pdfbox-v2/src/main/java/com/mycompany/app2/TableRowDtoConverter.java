package com.mycompany.app2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for converting between TableRow and TableRowDto objects
 * Provides static methods for easy conversion in both directions
 */
public class TableRowDtoConverter {
    
    /**
     * Converts a single TableRow to TableRowDto
     * @param tableRow TableRow object to convert
     * @return TableRowDto with data from TableRow
     */
    public static TableRowDto toDto(TableRow tableRow) {
        if (tableRow == null) {
            return new TableRowDto();
        }
        return new TableRowDto(tableRow);
    }
    
    /**
     * Converts a list of TableRow objects to TableRowDto objects
     * @param tableRows List of TableRow objects
     * @return List of TableRowDto objects
     */
    public static List<TableRowDto> toDtoList(List<TableRow> tableRows) {
        if (tableRows == null) {
            return List.of();
        }
        
        return tableRows.stream()
                .map(TableRowDtoConverter::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Converts a list of TableRow objects to TableRowDto objects with filtering
     * Only includes rows that have content
     * @param tableRows List of TableRow objects
     * @return List of TableRowDto objects (filtered for content)
     */
    public static List<TableRowDto> toDtoListFiltered(List<TableRow> tableRows) {
        if (tableRows == null) {
            return List.of();
        }
        
        return tableRows.stream()
                .map(TableRowDtoConverter::toDto)
                .filter(TableRowDto::hasContent)  // Only include rows with content
                .collect(Collectors.toList());
    }
    
    /**
     * Converts TableRowDto back to TableRow (if needed)
     * @param dto TableRowDto to convert
     * @return TableRow object
     */
    public static TableRow fromDto(TableRowDto dto) {
        if (dto == null) {
            return new TableRow();
        }
        
        TableRow tableRow = new TableRow();
        tableRow.setCell(0, dto.getCol1());
        tableRow.setCell(1, dto.getCol2());
        tableRow.setCell(2, dto.getCol3());
        tableRow.setCell(3, dto.getCol4());
        tableRow.setCell(4, dto.getCol5());
        tableRow.setCell(5, dto.getCol6());
        
        return tableRow;
    }
    
    /**
     * Prints a list of DTOs in a formatted way
     * @param dtos List of TableRowDto objects to print
     */
    public static void printDtos(List<TableRowDto> dtos) {
        System.out.println("=== TABLE DATA (" + dtos.size() + " rows) ===");
        for (int i = 0; i < dtos.size(); i++) {
            System.out.printf("Row %d: %s%n", i + 1, dtos.get(i).toDisplayString());
        }
    }
    
    /**
     * Filters DTOs to only include complete rows (all columns have content)
     * @param dtos List of TableRowDto objects
     * @return Filtered list containing only complete rows
     */
    public static List<TableRowDto> filterCompleteRows(List<TableRowDto> dtos) {
        if (dtos == null) {
            return List.of();
        }
        
        return dtos.stream()
                .filter(TableRowDto::isComplete)
                .collect(Collectors.toList());
    }
    
    /**
     * Gets DTOs where a specific column matches a pattern
     * @param dtos List of TableRowDto objects
     * @param columnIndex Column index to check (0-5)
     * @param pattern Regex pattern to match
     * @return Filtered list of matching DTOs
     */
    public static List<TableRowDto> filterByColumn(List<TableRowDto> dtos, int columnIndex, String pattern) {
        if (dtos == null || pattern == null) {
            return List.of();
        }
        
        return dtos.stream()
                .filter(dto -> dto.getColumnByIndex(columnIndex).matches(pattern))
                .collect(Collectors.toList());
    }
}