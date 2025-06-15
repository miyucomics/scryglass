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
	private var previousFrame: Map<Int, Icon> = emptyMap()
	private var workingFrame: MutableMap<Int, Icon> = mutableMapOf()
	private val deltas: MutableList<Delta> = mutableListOf()

	constructor()

	constructor (icons: Map<Int, Icon>) {
		this.previousFrame = icons
		this.workingFrame = previousFrame.toMutableMap()
	}

	data class Delta(val addedOrChanged: Map<Int, Icon>, val removed: Set<Int>)

	// Modify working frame

	fun setIcon(index: Int, icon: Icon) {
		workingFrame[index] = icon
	}

	fun removeIcon(index: Int) {
		workingFrame.remove(index)
	}

	fun peek(): List<Int> {
		return workingFrame.keys.toList()
	}

	// Called when player joins

	fun prime(player: ServerPlayerEntity) {
		val buf = PacketByteBufs.create()
		buf.writeNbt(this.serialize())
		ServerPlayNetworking.send(player, ScryglassMain.PRIMER_CHANNEL, buf)
	}

	// Called to create a new frame and advance deltas

	fun flip() {
		val addedOrChanged = mutableMapOf<Int, Icon>()
		val removed = mutableSetOf<Int>()

		for ((index, icon) in workingFrame) {
			if (previousFrame[index] != icon) {
				addedOrChanged[index] = icon
			}
		}

		for (index in previousFrame.keys) {
			if (!workingFrame.containsKey(index)) {
				removed.add(index)
			}
		}

		deltas.add(Delta(addedOrChanged, removed))
		previousFrame = workingFrame.toMap()
		workingFrame = previousFrame.toMutableMap()
	}

	// Send accumulated deltas to client

	fun push(player: ServerPlayerEntity) {
		if (deltas.isEmpty())
			return

		val buf = PacketByteBufs.create()
		val compound = NbtCompound()
		val deltasList = NbtList()

		for (delta in deltas) {
			val deltaNbt = NbtCompound()
			val addedList = NbtList()
			for ((index, icon) in delta.addedOrChanged) {
				val iconNbt = icon.toNBT()
				iconNbt.putInt("index", index)
				iconNbt.putString("type", ICON_REGISTRY.getId(icon.type)!!.toString())
				addedList.add(iconNbt)
			}

			val removedList = NbtList()
			for (index in delta.removed)
				removedList.add(NbtInt.of(index))

			deltaNbt.put("added", addedList)
			deltaNbt.put("removed", removedList)
			deltasList.add(deltaNbt)
		}

		compound.put("deltas", deltasList)
		buf.writeNbt(compound)
		ServerPlayNetworking.send(player, ScryglassMain.UPDATE_CHANNEL, buf)

		deltas.clear()
	}

	// Serialize

	fun serialize(): NbtCompound {
		val compound = NbtCompound()
		val list = NbtList()
		for ((index, icon) in previousFrame) {
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