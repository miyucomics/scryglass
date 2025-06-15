package miyucomics.scryglass.icons

import net.minecraft.client.gui.DrawContext
import net.minecraft.nbt.NbtCompound

interface Icon {
	val type: IconType<out Icon>
	fun render(drawContext: DrawContext, deltaTime: Float)
	fun toNBT(): NbtCompound
	fun tick(): Boolean
}

interface IconType<T : Icon> {
	fun fromNBT(compound: NbtCompound): T
}