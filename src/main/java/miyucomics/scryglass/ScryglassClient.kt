package miyucomics.scryglass

import miyucomics.scryglass.inits.ScryglassNetworking
import miyucomics.scryglass.storage.ClientStorage
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient

class ScryglassClient : ClientModInitializer {
	override fun onInitializeClient() {
		ScryglassNetworking.clientInit()
		ClientPlayConnectionEvents.JOIN.register { handler, sender, client -> ScryglassNetworking.sendDimensions() }
		HudRenderCallback.EVENT.register { drawContext, tickDelta ->
			ClientStorage.viewforms.forEach { it.render(drawContext, tickDelta) }
		}
	}

	companion object {
		val CLIENT: MinecraftClient = MinecraftClient.getInstance()
	}
}