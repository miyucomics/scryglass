package miyucomics.scryglass.inits

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import miyucomics.scryglass.ScryglassMain
import miyucomics.scryglass.actions.OpDrawText
import miyucomics.scryglass.actions.OpGetWindowSize
import miyucomics.scryglass.actions.OpRemoveViewform
import net.minecraft.registry.Registry

object ScryglassPatterns {
	@JvmStatic
	fun init() {
		register("text_viewform", "aaqdwdwd", HexDir.NORTH_EAST, OpDrawText())
		register("get_window_size", "aawawaa", HexDir.NORTH_EAST, OpGetWindowSize())
		register("remove_viewform", "awawa", HexDir.WEST, OpRemoveViewform())
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(HexActions.REGISTRY, ScryglassMain.id(name), ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), action))
}