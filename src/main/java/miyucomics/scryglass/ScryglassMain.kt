package miyucomics.scryglass

import miyucomics.scryglass.inits.ScryglassNetworking
import miyucomics.scryglass.inits.ScryglassPatterns
import miyucomics.scryglass.state.ViewformData
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

class ScryglassMain : ModInitializer {
	override fun onInitialize() {
		ViewformData.init()
		ScryglassNetworking.serverInit()
		ScryglassPatterns.init()
	}

	companion object {
		fun id(string: String) = Identifier("scryglass", string)
	}
}