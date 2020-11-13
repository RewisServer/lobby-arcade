package dev.volix.rewinside.odyssey.lobby.arcade.helper

import java.awt.Point

/**
 * @author Benedikt Wüller
 */

fun getRandomPoint(minX: Int, minY: Int, maxX: Int, maxY: Int, excludePoints: Collection<Point>?): Point {
    var point: Point
    var tries = 0

    do {
        point = Point(
                Math.round(Math.random() * (maxX - minX - 1)).toInt() + minX,
                Math.round(Math.random() * (maxY - minY - 1)).toInt() + minY
        )
        tries++
    } while (tries < 10 && excludePoints != null && excludePoints.contains(point))

    return point
}
