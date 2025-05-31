package miyucomics.scryglass.viewforms

import miyucomics.scryglass.ScryglassClient
import miyucomics.scryglass.state.Viewform
import miyucomics.scryglass.state.ViewformType
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.sin

class TextViewform(
	private val text: Text,
	private val position: Vec3d,
	override var age: Int = 0
) : Viewform {
	override val type: ViewformType<out Viewform> = TYPE

	companion object {
		val TYPE = object : ViewformType<TextViewform> {
			override fun fromNBT(nbt: NbtCompound): TextViewform {
				val text = Text.Serializer.fromJson(nbt.getString("text"))!!
				val pos = Vec3d(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"))
				return TextViewform(text, pos)
			}
		}
	}

	override fun render(drawContext: DrawContext, deltaTime: Float) {
		val matrices = drawContext.matrices
		matrices.push()
		matrices.translate(
			ScryglassClient.CLIENT.window.scaledWidth / 2.0,
			ScryglassClient.CLIENT.window.scaledHeight / 2.0,
			0.0
		)
		val width = MinecraftClient.getInstance().textRenderer.getWidth(text)
		drawContext.drawText(
			MinecraftClient.getInstance().textRenderer,
			text,
			position.x.toInt() - width / 2,
			position.y.toInt(),
			0,
			true
		)
		matrices.pop()
	}

	override fun toNBT(): NbtCompound {
		val compound = NbtCompound()
		compound.putString("text", Text.Serializer.toJson(text))
		compound.putDouble("x", position.x)
		compound.putDouble("y", position.y)
		compound.putDouble("z", position.z)
		return compound
	}
}