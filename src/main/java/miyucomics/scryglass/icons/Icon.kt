package miyucomics.scryglass.icons

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.RotationAxis

abstract class Icon(iconType: IconType<out Icon>) {
	var age: Int = 0
	var scale: Float = 1f
	var rotation: Float = 0f
	val type: IconType<out Icon> = iconType

	fun tick(): Boolean {
		age += 1
		return age > 10
	}

	fun render(drawContext: DrawContext, deltaTime: Float) {
		val matrices = drawContext.matrices
		matrices.push()
		matrices.translate(MinecraftClient.getInstance().window.scaledWidth / 2.0, MinecraftClient.getInstance().window.scaledHeight / 2.0, 0.0)
		matrices.scale(scale, scale, scale)
		matrices.multiply(RotationAxis.NEGATIVE_Z.rotation(rotation * 6.283f))
		renderCustom(matrices, drawContext, deltaTime)
		matrices.pop()
	}

	fun toNBT(): NbtCompound {
		val compound = NbtCompound()
		compound.putInt("age", age)
		compound.putFloat("scale", scale)
		compound.putFloat("rotation", rotation)
		writeCustomNBT(compound)
		return compound
	}

	open fun readNBT(compound: NbtCompound) {
		age = compound.getInt("age")
		scale = compound.getFloat("scale")
		rotation = compound.getFloat("rotation")
		readCustomNBT(compound)
	}

	protected abstract fun writeCustomNBT(compound: NbtCompound)
	protected abstract fun readCustomNBT(compound: NbtCompound)
	protected abstract fun renderCustom(matrices: MatrixStack, drawContext: DrawContext, deltaTime: Float)
}

fun <T> iconType(factory: (IconType<T>) -> T): IconType<T> where T : Icon {
	return object : IconType<T> {
		override fun fromNBT(compound: NbtCompound): T {
			val instance = factory(this)
			instance.readNBT(compound)
			return instance
		}
	}
}

interface IconType<T : Icon> {
	fun fromNBT(compound: NbtCompound): T
}