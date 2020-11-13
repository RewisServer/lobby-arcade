package dev.volix.rewinside.odyssey.lobby.arcade.component

import dev.volix.rewinside.odyssey.common.frames.alignment.Alignment
import dev.volix.rewinside.odyssey.common.frames.component.ColorComponent
import dev.volix.rewinside.odyssey.common.frames.component.CompoundComponent
import dev.volix.rewinside.odyssey.common.frames.component.TextComponent
import dev.volix.rewinside.odyssey.common.frames.resource.font.FontAdapter
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import kotlin.math.round

/**
 * @author Benedikt Wüller
 */
class WaitForInputComponent(position: Point, dimensions: Dimension, fontAdapter: FontAdapter) : CompoundComponent(position, dimensions) {

    companion object {
        const val FONT_NAME = "JetBrainsMono-ExtraBold"
    }

    init {
        val fontSize = round(this.dimensions.width * 0.065).toFloat()
        val font = fontAdapter.get(FONT_NAME, fontSize);

        this.addComponent(ColorComponent(this.position, this.dimensions, Color(0, 0, 0, 128)))

        if (this.dimensions.width > this.dimensions.height) {
            this.addComponent(TextComponent(
                    Point(this.position.x + this.dimensions.width / 2, (this.position.y + this.dimensions.height / 2 - fontSize / 2).toInt()),
                    "Drücke eine beliebige", Color.WHITE, font, Alignment.BOTTOM_CENTER
            ))

            this.addComponent(TextComponent(
                    Point(this.position.x + this.dimensions.width / 2, (this.position.y + this.dimensions.height / 2 + fontSize / 2).toInt()),
                    "Taste zum Starten", Color.WHITE, font, Alignment.TOP_CENTER
            ))
        } else {
            this.addComponent(TextComponent(
                    Point(this.position.x + this.dimensions.width / 2, (this.position.y + this.dimensions.height / 2 - fontSize).toInt()),
                    "Drücke eine", Color.WHITE, font, Alignment.BOTTOM_CENTER
            ))

            this.addComponent(TextComponent(
                    Point(this.position.x + this.dimensions.width / 2, this.position.y + this.dimensions.height / 2),
                    "beliebige Taste", Color.WHITE, font, Alignment.CENTER_CENTER
            ))

            this.addComponent(TextComponent(
                    Point(this.position.x + this.dimensions.width / 2, (this.position.y + this.dimensions.height / 2 + fontSize).toInt()),
                    "zum Starten", Color.WHITE, font, Alignment.TOP_CENTER
            ))
        }
    }

}
