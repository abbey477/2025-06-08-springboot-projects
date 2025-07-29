package com.mycompany.app4.domain;

public class BoundaryFactory {
    /**
     * Creates a TableBoundaryDTO with specified coordinates.
     *
     * @param left   Left boundary coordinate
     * @param right  Right boundary coordinate
     * @param top    Top boundary coordinate
     * @param bottom Bottom boundary coordinate
     * @return A new TableBoundaryDTO instance
     */
    public TableBoundaryDTO createTableBoundary(float left, float right,
                                                float top, float bottom) {
        return new TableBoundaryDTO(left, right, top, bottom);
    }

    public static TableBoundaryDTO getPageOneToFiveBoundary() {
        return TableBoundaryDTO.builder()
                .left(50f)
                .right(590f)
                .top(140f)
                .bottom(700f)
                .build();
    }

    public static TableBoundaryDTO getPageSixBoundary() {
        //  new TableBoundaries(50f, 590f, 140f, 600f);
        return TableBoundaryDTO.builder()
                .left(50f)
                .right(590f)
                .top(140f)
                .bottom(600f)
                .build();
    }
}