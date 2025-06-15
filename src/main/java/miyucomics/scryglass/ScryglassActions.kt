package miyucomics.scryglass

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import miyucomics.scryglass.actions.*
import net.minecraft.registry.Registry

object ScryglassActions {
	@JvmStatic
	fun init() {
		register("get_window_size", "aawawaa", HexDir.NORTH_EAST, OpGetWindowSize())
		register("get_icons", "dwdwd", HexDir.EAST, OpGetIcons())
		register("remove_icon", "awawa", HexDir.WEST, OpRemoveIcons())

		register("draw_text", "aaqdwdwd", HexDir.NORTH_EAST, OpDrawText())
		register("draw_rect", "aaqdwdwdewaq", HexDir.NORTH_EAST, OpDrawRect())
		register("draw_line", "aaqdwdwdeww", HexDir.NORTH_EAST, OpDrawLine())

		register("flip", "eqqqqqww", HexDir.NORTH_EAST, OpFlip())
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(
			HexActions.REGISTRY, ScryglassMain.id(name),
			ActionRegistryEntry(HexPattern.Companion.fromAngles(signature, startDir), action)
		)
}