package miyucomics.scryglass.icons

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.nbt.NbtCompound
import org.joml.Vector3f

class RectIcon(iconType: IconType<RectIcon>) : BaseIcon(iconType) {
	private lateinit var position: Vector3f
	private lateinit var size: Vector3f
	private var color: Int = 0

	constructor(position: Vector3f, size: Vector3f, color: Int) : this(TYPE) {
		this.position = position
		this.size = size
		this.color = color
	}

	override fun render(drawContext: DrawContext, deltaTime: Float) {
		val matrices = drawContext.matrices
		matrices.push()
		matrices.translate(MinecraftClient.getInstance().window.scaledWidth / 2.0, MinecraftClient.getInstance().window.scaledHeight / 2.0, 0.0)

		val buffer = drawContext.vertexConsumers.getBuffer(RenderLayer.getGuiOverlay())
		val matrix = matrices.peek().positionMatrix

		buffer.vertex(matrix, position.x, position.y + size.y, 0f).color(color).next()
		buffer.vertex(matrix, position.x + size.x, position.y + size.y, 0f).color(color).next()
		buffer.vertex(matrix, position.x + size.x, position.y, 0f).color(color).next()
		buffer.vertex(matrix, position.x, position.y, 0f).color(color).next()

		drawContext.draw()
		matrices.pop()
	}

	override fun writeCustomNBT(compound: NbtCompound) {
		compound.putFloat("x", position.x)
		compound.putFloat("y", position.y)
		compound.putFloat("z", position.z)
		compound.putFloat("width", size.x)
		compound.putFloat("height", size.y)
		compound.putFloat("depth", size.z)
		compound.putInt("color", color)
	}

	override fun readCustomNBT(compound: NbtCompound) {
		position = Vector3f(compound.getFloat("x"), compound.getFloat("y"), compound.getFloat("z"))
		size = Vector3f(compound.getFloat("width"), compound.getFloat("height"), compound.getFloat("depth"))
		color = compound.getInt("color")
	}

	companion object {
		val TYPE = iconType(::RectIcon)
	}
}