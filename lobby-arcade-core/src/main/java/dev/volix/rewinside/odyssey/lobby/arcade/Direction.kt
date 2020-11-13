package dev.volix.rewinside.odyssey.lobby.arcade

import java.awt.Point
import java.awt.geom.Point2D

/**
 * @author Benedikt Wüller
 */
enum class Direction {

    NORTH,
    EAST,
    SOUTH,
    WEST;

    /**
     * @return the opposite direction.
     */
    val opposite; get() = when (this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
    }

    /**
     * Returns whether the given direction is opposite of this one.
     *
     * @param other the direction to check.
     * @return whether the given direction is opposite of this one.
     */
    fun isOppositeOf(other: Direction?) = this.opposite == other

    /**
     * Returns whether the given direction is perpendicular to this one.
     *
     * @param other the direction to check.
     * @return whether the directions are perpendicular.
     */
    fun isPerpendicularTo(other: Direction?) = when (this) {
        NORTH, SOUTH -> other == EAST || other == WEST
        EAST, WEST -> other == NORTH || other == SOUTH
        else -> false
    }

    /**
     * Move the given amount of steps from the origin point in this direction.
     *
     * @param from the point to move from.
     * @param amount the amount of steps to move.
     * @return the resulting position.
     */
    fun move(from: Point, amount: Int) = when (this) {
        NORTH -> Point(from.x, from.y - amount)
        SOUTH -> Point(from.x, from.y + amount)
        WEST -> Point(from.x - amount, from.y)
        EAST -> Point(from.x + amount, from.y)
    }

    /**
     * Move the given amount from the origin point in this direction.
     *
     * @param from the point to move from.
     * @param amount the amount to move.
     * @return the resulting position.
     */
    fun move(from: Point2D, amount: Double) = when (this) {
        NORTH -> Point2D.Double(from.x, from.y - amount)
        SOUTH -> Point2D.Double(from.x, from.y + amount)
        WEST -> Point2D.Double(from.x - amount, from.y)
        EAST -> Point2D.Double(from.x + amount, from.y)
    }

    companion object {
        /**
         * Determines and returns the direction from the first to the second point.
         * When the two points are diagonal to each other, only one main direction
         * will be returned (i.e. SOUTH-EAST -> EAST or NORTH-WEST -> WEST).
         *
         * @param from the origin position.
         * @param to the target position.
         * @return the determined direction or <code>null</code> if both points are equal.
         */
        @JvmStatic fun getDirection(from: Point, to: Point) = when {
            from.x < to.x -> EAST
            from.x > to.x -> WEST
            from.y < to.y -> SOUTH
            from.y > to.y -> NORTH
            else -> null
        }
    }

}
