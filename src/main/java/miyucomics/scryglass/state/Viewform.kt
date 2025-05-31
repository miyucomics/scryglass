package miyucomics.scryglass.state

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.nbt.NbtCompound

interface Viewform {
	fun render(drawContext: DrawContext, deltaTime: Float)
	fun toNBT(): NbtCompound

	var age: Int
	val type: ViewformType<out Viewform>
}

interface ViewformType<T : Viewform> {
	fun fromNBT(nbt: NbtCompound): T
}