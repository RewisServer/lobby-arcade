package dev.volix.rewinside.odyssey.lobby.arcade.standalone

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel

/**
 * @author Benedikt Wüller
 */
class FrameGameViewport(private val window: JFrame) : JPanel() {

    var sections = mutableListOf<Rectangle>()

    private var image: BufferedImage? = null

    var currentImage: BufferedImage? = null; set(value) {
        field = value
        this.updateSize()
    }

    var debug = false; set(value) {
        field = value
        this.background = if (value) Color.MAGENTA else Color.WHITE
    }

    init {
        this.updateSize()
    }

    private fun updateSize() {
        this.preferredSize = Dimension(this.currentImage?.width ?: 0, this.currentImage?.height ?: 0)
        this.size = this.preferredSize
        this.window.pack()
    }

    override fun paintComponent(context: Graphics) {
        super.paintComponent(context)

        if (this.currentImage == null) return
        if (this.image == null) {
            this.image = this.currentImage!!.getSubimage(0, 0, this.currentImage!!.width, this.currentImage!!.height)
        } else {
            val imageContext = image!!.createGraphics()

            for (section in this.sections) {
                imageContext.drawImage(
                        this.currentImage,
                        section.x, section.y, section.x + section.width, section.y + section.height,
                        section.x, section.y, section.x + section.width, section.y + section.height,
                        null
                )
            }
        }

        context.drawImage(this.image, 0, 0, this.width, this.height, 0, 0, this.image!!.width, this.image!!.height, null)

        if (this.debug) {
            for (section in this.sections) {
                context.color = Color.YELLOW
                context.drawRect(section.x, section.y, section.width, section.height)
            }
        }
    }

}
