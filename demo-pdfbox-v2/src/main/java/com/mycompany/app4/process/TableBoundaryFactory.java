package com.mycompany.app4.process;

import lombok.Builder;

public class TableBoundaryFactory {

    /**
    * Data Transfer Object representing the boundaries of a table.
    *
    * @param left   the left boundary coordinate
    * @param right  the right boundary coordinate
    * @param top    the top boundary coordinate
    * @param bottom the bottom boundary coordinate
    */
   @Builder(toBuilder = true)
   public record TableBoundaryDTO(float left,
                                  float right,
                                  float top,
                                  float bottom) {}


    public TableBoundaryDTO createTableBoundary(float left, float right,
                                                float top, float bottom) {
        return new TableBoundaryDTO(left, right, top, bottom);
    }

    public TableBoundaryDTO getPageOneToFiveBoundary() {
        return TableBoundaryDTO.builder()
                .left(50f)
                .right(590f)
                .top(140f)
                .bottom(700f)
                .build();
    }

    public TableBoundaryDTO getPageSixBoundary() {
        //  new TableBoundaries(50f, 590f, 140f, 600f);
        return TableBoundaryDTO.builder()
                .left(50f)
                .right(590f)
                .top(140f)
                .bottom(600f)
                .build();
    }
}