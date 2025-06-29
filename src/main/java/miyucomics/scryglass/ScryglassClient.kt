package miyucomics.scryglass

import miyucomics.scryglass.ScryglassMain.Companion.DIMENSIONS_CHANNEL
import miyucomics.scryglass.state.ClientManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.client.MinecraftClient

class ScryglassClient : ClientModInitializer {
	override fun onInitializeClient() {
		ClientManager.init()
		ClientPlayConnectionEvents.JOIN.register { _, _, _ -> sendDimensions() }
		HudRenderCallback.EVENT.register { drawContext, tickDelta -> ClientManager.render(drawContext, tickDelta) }
	}

	companion object {
		@JvmStatic
		fun sendDimensions() {
			val buf = PacketByteBufs.create()
			buf.writeInt(MinecraftClient.getInstance().window.scaledWidth)
			buf.writeInt(MinecraftClient.getInstance().window.scaledHeight)
			ClientPlayNetworking.send(DIMENSIONS_CHANNEL, buf)
		}
	}
}