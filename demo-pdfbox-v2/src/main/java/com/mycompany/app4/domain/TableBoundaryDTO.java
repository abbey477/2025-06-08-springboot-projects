package com.mycompany.app4.domain;

import lombok.Builder;

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