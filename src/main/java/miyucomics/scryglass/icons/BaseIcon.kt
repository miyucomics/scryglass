package miyucomics.scryglass.icons

import net.minecraft.nbt.NbtCompound

abstract class BaseIcon(iconType: IconType<out Icon>) : Icon {
	var age: Int = 0
	override val type: IconType<out Icon> = iconType

	override fun tick(): Boolean {
		age += 1
		return age > 20
	}

	final override fun toNBT(): NbtCompound {
		val compound = NbtCompound()
		compound.putInt("age", age)
		writeCustomNBT(compound)
		return compound
	}

	open fun readNBT(compound: NbtCompound) {
		age = compound.getInt("age")
		readCustomNBT(compound)
	}

	protected abstract fun writeCustomNBT(compound: NbtCompound)
	protected abstract fun readCustomNBT(compound: NbtCompound)
}

fun <T> iconType(factory: (IconType<T>) -> T): IconType<T> where T : BaseIcon {
	return object : IconType<T> {
		override fun fromNBT(compound: NbtCompound): T {
			val instance = factory(this)
			instance.readNBT(compound)
			return instance
		}
	}
}