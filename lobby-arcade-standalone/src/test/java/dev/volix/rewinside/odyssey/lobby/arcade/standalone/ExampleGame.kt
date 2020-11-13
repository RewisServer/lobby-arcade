package dev.volix.rewinside.odyssey.lobby.arcade.standalone

import dev.volix.rewinside.odyssey.common.frames.color.ColorTransformer
import dev.volix.rewinside.odyssey.common.frames.color.MinecraftColorPalette
import dev.volix.rewinside.odyssey.common.frames.component.ColorComponent
import dev.volix.rewinside.odyssey.common.frames.component.CompoundComponent
import dev.volix.rewinside.odyssey.common.frames.component.ImageComponent
import dev.volix.rewinside.odyssey.lobby.arcade.Direction
import dev.volix.rewinside.odyssey.lobby.arcade.FrameGame
import dev.volix.rewinside.odyssey.lobby.arcade.GameState
import dev.volix.rewinside.odyssey.lobby.arcade.InputKey
import dev.volix.rewinside.odyssey.common.frames.resource.image.ImageFileAdapter
import java.awt.Color
import java.awt.Dimension
import java.awt.Point

class ExampleGame(transformer: ColorTransformer)
    : FrameGame(Dimension(256, 200), initialUpdateInterval = 20L, transformer = transformer) {

    private val imageAdapter = ImageFileAdapter("resources")
    private val image = this.imageAdapter.get("rgb_full")

    private val component = CompoundComponent(Point(), this.canvasDimensions)
    private val colorComponent = ColorComponent(Point(), Dimension(4, this.canvasDimensions.height), Color.WHITE)

    private val idleComponent = ImageComponent(Point(), this.canvasDimensions, this.imageAdapter.get("idle"))

    private var direction: Direction = Direction.EAST

    init {
        val imageComponent = ImageComponent(Point(), this.viewportDimension, this.image)

        this.component.addComponent(imageComponent)
        this.component.addComponent(colorComponent)
    }

    override fun getRenderComponent(state: GameState) = when (state) {
        GameState.RUNNING -> this.component
        else -> this.idleComponent
    }

    override fun onUpdate(currentTime: Long, delta: Long): Boolean {
        if (this.state != GameState.RUNNING) return false

        this.colorComponent.position.location = this.direction.move(this.colorComponent.position, 2)
        if (this.direction == Direction.EAST && this.colorComponent.position.x >= this.canvasDimensions.width) {
            this.colorComponent.position.x = -2
        } else if (this.direction == Direction.WEST && this.colorComponent.position.x < 0) {
            this.colorComponent.position.x = this.canvasDimensions.width
        } else if (this.direction == Direction.SOUTH && this.colorComponent.position.y >= this.canvasDimensions.height) {
            this.colorComponent.position.y = -2
        } else if (this.direction == Direction.NORTH && this.colorComponent.position.y < 0) {
            this.colorComponent.position.y = this.canvasDimensions.height
        }

        return true
    }

    private fun randomizeColor() {
        val color = Color(
                (Math.random() * 255).toInt(),
                (Math.random() * 255).toInt(),
                (Math.random() * 255).toInt()
        )

        this.colorComponent.color = color
    }

    override fun onKeyDown(key: InputKey, currentTime: Long) {
        if (key == InputKey.SPACE) {
            this.randomizeColor()
            return
        }

        val newDirection = key.direction ?: Direction.EAST
        if (this.direction == newDirection) return
        this.direction = newDirection

        when (this.direction) {
            Direction.EAST -> {
                this.colorComponent.position.location = Point(-2, 0)
                this.colorComponent.dimensions.width = 4
                this.colorComponent.dimensions.height = this.canvasDimensions.height
            }
            Direction.WEST -> {
                this.colorComponent.position.location = Point(this.canvasDimensions.width, 0)
                this.colorComponent.dimensions.width = 4
                this.colorComponent.dimensions.height = this.canvasDimensions.height
            }
            Direction.SOUTH -> {
                this.colorComponent.position.location = Point(0, -2)
                this.colorComponent.dimensions.width = this.canvasDimensions.width
                this.colorComponent.dimensions.height = 4
            }
            Direction.NORTH -> {
                this.colorComponent.position.location = Point(0, this.canvasDimensions.height)
                this.colorComponent.dimensions.width = this.canvasDimensions.width
                this.colorComponent.dimensions.height = 4
            }
        }
    }

    override fun onKeyUp(key: InputKey, currentTime: Long) = Unit

    override fun onKeyRepeat(key: InputKey, currentTime: Long) {
        if (key == InputKey.SPACE) {
            this.randomizeColor()
            return
        }
    }

}

fun main() {
    FrameGameApplication("Example") { ExampleGame(MinecraftColorPalette()) }.start()
}
