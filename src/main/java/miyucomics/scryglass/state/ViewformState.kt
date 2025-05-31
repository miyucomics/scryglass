package miyucomics.scryglass.state

import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.putList
import miyucomics.scryglass.inits.ScryglassNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

class ViewformState {
	val viewforms: MutableMap<Int, Viewform> = mutableMapOf()
	private var changed = false

	fun setViewform(index: Int, viewform: Viewform) {
		viewforms[index] = viewform
		changed = true
	}

	fun removeViewform(index: Int) {
		if (viewforms.remove(index) != null)
			changed = true
	}

	fun updateIfNeeded(player: ServerPlayerEntity) {
		if (changed) {
			val buf = PacketByteBufs.create()
			buf.writeNbt(serialize())
			ServerPlayNetworking.send(player, ScryglassNetworking.VIEWFORMS_CHANNEL, buf)
			changed = false
		}
	}

	fun serialize(): NbtCompound {
		val compound = NbtCompound()

		val viewformNbts = NbtList()
		viewforms.forEach { index, viewform ->
			val serializedForm = NbtCompound()
			val viewformType = ViewformData.VIEWFORM_REGISTRY.getId(viewform.type)!!
			serializedForm.putInt("index", index)
			serializedForm.putString("type", viewformType.toString())
			serializedForm.put("data", viewform.toNBT())
			viewformNbts.add(serializedForm)
		}

		compound.putList("viewforms", viewformNbts)
		return compound
	}

	companion object {
		@JvmStatic
		fun deserialize(compound: NbtCompound): ViewformState {
			val new = ViewformState()
			val viewformNbts = compound.getList("viewforms", NbtElement.COMPOUND_TYPE.toInt())
			viewformNbts.forEach {
				val viewformNbt = it.asCompound
				val type = ViewformData.VIEWFORM_REGISTRY.get(Identifier(viewformNbt.getString("type")))
				if (type != null)
					new.viewforms[viewformNbt.getInt("index")] = type.fromNBT(viewformNbt.getCompound("data"))
			}
			return new
		}
	}
}