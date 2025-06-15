package miyucomics.scryglass.icons

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import org.joml.Vector3f

class TextIcon(iconType: IconType<TextIcon>) : BaseIcon(iconType) {
	private lateinit var text: Text
	private lateinit var position: Vector3f

	constructor(text: Text, position: Vector3f) : this(TYPE) {
		this.text = text
		this.position = position
	}

	override fun render(drawContext: DrawContext, deltaTime: Float) {
		val matrices = drawContext.matrices
		matrices.push()
		matrices.translate(MinecraftClient.getInstance().window.scaledWidth / 2.0, MinecraftClient.getInstance().window.scaledHeight / 2.0, 0.0)
		val width = MinecraftClient.getInstance().textRenderer.getWidth(text)
		val height = MinecraftClient.getInstance().textRenderer.fontHeight
		drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, position.x.toInt() - width / 2, position.y.toInt() - height / 2, 0xff_ffffff.toInt())
		matrices.pop()
	}

	override fun writeCustomNBT(compound: NbtCompound) {
		compound.putString("text", Text.Serializer.toJson(text))
		compound.putFloat("x", position.x)
		compound.putFloat("y", position.y)
		compound.putFloat("z", position.z)
	}

	override fun readCustomNBT(compound: NbtCompound) {
		text = Text.Serializer.fromJson(compound.getString("text"))!!
		position = Vector3f(compound.getFloat("x"), compound.getFloat("y"), compound.getFloat("z"))
	}

	companion object {
		val TYPE = iconType(::TextIcon)
	}
}