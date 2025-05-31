package miyucomics.scryglass.inits

import miyucomics.scryglass.PlayerEntityMinterface
import miyucomics.scryglass.ScryglassClient.Companion.CLIENT
import miyucomics.scryglass.ScryglassMain
import miyucomics.scryglass.state.ViewformData
import miyucomics.scryglass.state.ViewformState
import miyucomics.scryglass.storage.ClientStorage
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object ScryglassNetworking {
	val DIMENSIONS_CHANNEL = ScryglassMain.id("dimensions")
	val VIEWFORMS_CHANNEL = ScryglassMain.id("viewforms")

	fun serverInit() {
		ServerPlayNetworking.registerGlobalReceiver(DIMENSIONS_CHANNEL) { _, player, _, buf, _ ->
			(player as PlayerEntityMinterface).setWindowSize(Pair(buf.readInt().toDouble(), buf.readInt().toDouble()))
			val buf = PacketByteBufs.create()
			buf.writeNbt((player as PlayerEntityMinterface).getViewformState().serialize())
			ServerPlayNetworking.send(player, VIEWFORMS_CHANNEL, buf)
		}
	}

	fun clientInit() {
		ClientPlayNetworking.registerGlobalReceiver(VIEWFORMS_CHANNEL) { _, _, buf, _ ->
			ClientStorage.viewforms.clear()
			ViewformState.deserialize(buf.readNbt()!!).viewforms.values.forEach { ClientStorage.viewforms.add(it) }
		}
	}

	@JvmStatic
	fun sendDimensions() {
		val buf = PacketByteBufs.create()
		buf.writeInt(CLIENT.window.scaledWidth)
		buf.writeInt(CLIENT.window.scaledHeight)
		ClientPlayNetworking.send(DIMENSIONS_CHANNEL, buf)
	}
}