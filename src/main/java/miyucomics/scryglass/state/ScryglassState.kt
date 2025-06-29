package miyucomics.scryglass.state

import at.petrak.hexcasting.api.utils.asCompound
import miyucomics.scryglass.ScryglassMain
import miyucomics.scryglass.ScryglassMain.Companion.ICON_REGISTRY
import miyucomics.scryglass.icons.Icon
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtInt
import net.minecraft.nbt.NbtList
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class ScryglassState {
	private val frame: MutableMap<Int, Icon>
	private val additions: MutableMap<Int, Icon> = mutableMapOf()
	private val removals: MutableList<Int> = mutableListOf()

	constructor() : this(mutableMapOf())

	constructor(frame: Map<Int, Icon>) {
		this.frame = frame.toMutableMap()
	}

	fun peek(): MutableMap<Int, Icon> {
		return frame
	}

	fun get(index: Int): Icon? {
		return frame[index]
	}

	fun setIcon(index: Int, icon: Icon) {
		frame[index] = icon
		additions[index] = icon
		removals.remove(index)
	}

	fun removeIcon(index: Int) {
		frame.remove(index)
		additions.remove(index)
		removals.add(index)
	}

	fun tick() {
		val iterator = frame.entries.iterator()
		while (iterator.hasNext()) {
			val entry = iterator.next()
			if (entry.value.tick())
				iterator.remove()
		}
	}

	fun prime(player: ServerPlayerEntity) {
		val buf = PacketByteBufs.create()
		buf.writeNbt(this.serialize())
		ServerPlayNetworking.send(player, ScryglassMain.PRIMER_CHANNEL, buf)
	}

	fun push(player: ServerPlayerEntity) {
		if (additions.isEmpty() && removals.isEmpty())
			return

		val deltaNbt = NbtCompound()

		val addedList = NbtList()
		for ((index, icon) in additions) {
			val iconNbt = icon.toNBT()
			iconNbt.putInt("index", index)
			iconNbt.putString("type", ICON_REGISTRY.getId(icon.type)!!.toString())
			addedList.add(iconNbt)
		}
		deltaNbt.put("added", addedList)

		val removedList = NbtList()
		for (index in removals) {
			removedList.add(NbtInt.of(index))
		}
		deltaNbt.put("removed", removedList)

		val buf = PacketByteBufs.create()
		val wrapper = NbtCompound()
		wrapper.put("deltas", deltaNbt)
		buf.writeNbt(wrapper)
		ServerPlayNetworking.send(player, ScryglassMain.UPDATE_CHANNEL, buf)

		additions.clear()
		removals.clear()
	}

	fun serialize(): NbtCompound {
		val compound = NbtCompound()
		val list = NbtList()
		for ((index, icon) in frame) {
			val iconNbt = icon.toNBT()
			iconNbt.putInt("index", index)
			iconNbt.putString("type", ICON_REGISTRY.getId(icon.type)!!.toString())
			list.add(iconNbt)
		}
		compound.put("icons", list)
		return compound
	}

	companion object {
		@JvmStatic
		fun deserialize(compound: NbtCompound): ScryglassState {
			val frame = mutableMapOf<Int, Icon>()
			val iconsNbt = compound.getList("icons", NbtElement.COMPOUND_TYPE.toInt())
			for (element in iconsNbt) {
				val iconNbt = element.asCompound
				val typeId = Identifier(iconNbt.getString("type"))
				val type = ICON_REGISTRY.get(typeId) ?: continue
				val index = iconNbt.getInt("index")
				frame[index] = type.fromNBT(iconNbt)
			}
			return ScryglassState(frame)
		}
	}
}
