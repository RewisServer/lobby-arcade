package dev.volix.rewinside.odyssey.lobby.arcade.helper

import java.awt.Color
import kotlin.math.max
import kotlin.math.min

/**
 * @author Benedikt Wüller
 */

fun Color.darken(factor: Double): Color {
    return Color(
            max((this.red * factor).toInt(), 0),
            max((this.green * factor).toInt(), 0),
            max((this.blue * factor).toInt(), 0),
            this.alpha
    )
}

fun Color.brighten(factor: Double): Color {
    var r = this.red
    var g = this.green
    var b = this.blue
    val alpha = this.alpha

    val i = (1.0 / (1.0 - factor)).toInt()
    if (r == 0 && g == 0 && b == 0) return Color(i, i, i, alpha)

    if (r in 1 until i) r = i
    if (g in 1 until i) g = i
    if (b in 1 until i) b = i

    return Color(
            min((r / factor).toInt(), 255),
            min((g / factor).toInt(), 255),
            min((b / factor).toInt(), 255),
            alpha
    )
}
