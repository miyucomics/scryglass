package miyucomics.scryglass.icons

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import org.joml.Vector3f

class TextIcon(iconType: IconType<TextIcon>) : Icon(iconType) {
	lateinit var text: Text
	lateinit var position: Vector3f
	lateinit var justification: TextJustification

	constructor(text: Text, position: Vector3f, justification: TextJustification) : this(TYPE) {
		this.text = text
		this.position = position
		this.justification = justification
	}

	override fun renderCustom(matrices: MatrixStack, drawContext: DrawContext, deltaTime: Float) {
		val width = MinecraftClient.getInstance().textRenderer.getWidth(text)
		val xOffset = when (justification) {
			TextJustification.LEFT -> 0
			TextJustification.CENTER -> width / 2
			TextJustification.RIGHT -> width
		}

		val height = MinecraftClient.getInstance().textRenderer.fontHeight
		drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, position.x.toInt() - xOffset, position.y.toInt() - height / 2, 0xff_ffffff.toInt())
	}

	override fun writeCustomNBT(compound: NbtCompound) {
		compound.putString("text", Text.Serializer.toJson(text))
		compound.putFloat("x", position.x)
		compound.putFloat("y", position.y)
		compound.putFloat("z", position.z)
		compound.putInt("justification", justification.ordinal)
	}

	override fun readCustomNBT(compound: NbtCompound) {
		text = Text.Serializer.fromJson(compound.getString("text"))!!
		position = Vector3f(compound.getFloat("x"), compound.getFloat("y"), compound.getFloat("z"))
		justification = enumValues<TextJustification>()[compound.getInt("justification")]
	}

	companion object {
		val TYPE = iconType(::TextIcon)
	}
}

enum class TextJustification {
	CENTER,
	LEFT,
	RIGHT
}