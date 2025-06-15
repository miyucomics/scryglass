package miyucomics.scryglass.state

import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.api.utils.asInt
import miyucomics.scryglass.ScryglassMain.Companion.ICON_REGISTRY
import miyucomics.scryglass.ScryglassMain.Companion.PRIMER_CHANNEL
import miyucomics.scryglass.ScryglassMain.Companion.UPDATE_CHANNEL
import miyucomics.scryglass.icons.Icon
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.DrawContext
import net.minecraft.nbt.NbtElement
import net.minecraft.util.Identifier

object ClientManager {
	var baseFrame: Map<Int, Icon> = emptyMap()
	var transientFrames: List<Map<Int, Icon>> = emptyList()

	fun init() {
		ClientPlayNetworking.registerGlobalReceiver(PRIMER_CHANNEL) { client, _, buf, _ ->
			val nbt = buf.readNbt() ?: return@registerGlobalReceiver
			val frame = mutableMapOf<Int, Icon>()

			val iconsNbt = nbt.getList("icons", NbtElement.COMPOUND_TYPE.toInt())
			for (element in iconsNbt) {
				val iconNbt = element.asCompound
				val typeId = Identifier(iconNbt.getString("type"))
				val type = ICON_REGISTRY.get(typeId) ?: continue
				val index = iconNbt.getInt("index")
				frame[index] = type.fromNBT(iconNbt)
			}

			client.execute {
				baseFrame = frame
				transientFrames = listOf(baseFrame)
			}
		}

		ClientPlayNetworking.registerGlobalReceiver(UPDATE_CHANNEL) { client, _, buf, _ ->
			val nbt = buf.readNbt() ?: return@registerGlobalReceiver
			val deltasNbt = nbt.getList("deltas", NbtElement.COMPOUND_TYPE.toInt())

			client.execute {
				var current = baseFrame
				val constructedFrames = mutableListOf<Map<Int, Icon>>()

				for (element in deltasNbt) {
					val deltaNbt = element.asCompound
					val addedList = deltaNbt.getList("added", NbtElement.COMPOUND_TYPE.toInt())
					val removedList = deltaNbt.getList("removed", NbtElement.INT_TYPE.toInt())

					val nextFrame = current.toMutableMap()

					for (addedElement in addedList) {
						val iconNbt = addedElement.asCompound
						val typeId = Identifier(iconNbt.getString("type"))
						val type = ICON_REGISTRY.get(typeId) ?: continue
						val index = iconNbt.getInt("index")
						nextFrame[index] = type.fromNBT(iconNbt)
					}

					for (removedElement in removedList)
						nextFrame.remove(removedElement.asInt)

					current = nextFrame
					constructedFrames.add(current)
				}

				if (constructedFrames.isNotEmpty()) {
					baseFrame = constructedFrames.last()
					transientFrames = constructedFrames
				}
			}
		}
	}

	fun render(drawContext: DrawContext, tickDelta: Float) {
		val frame = if (transientFrames.isEmpty())
			baseFrame
		else
			transientFrames[(tickDelta * transientFrames.size.toFloat()).toInt()]

		frame.forEach { it.value.render(drawContext, tickDelta) }
	}

	fun discardTransientFrames() {
		transientFrames = emptyList()
	}
}