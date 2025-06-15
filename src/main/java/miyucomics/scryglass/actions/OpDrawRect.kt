package miyucomics.scryglass.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.scryglass.ScryglassMain.Companion.floatVector
import miyucomics.scryglass.ScryglassMain.Companion.interpretColor
import miyucomics.scryglass.ScryglassMain.Companion.reflectY
import miyucomics.scryglass.icons.RectIcon
import miyucomics.scryglass.state.PlayerEntityMinterface
import net.minecraft.server.network.ServerPlayerEntity

class OpDrawRect : ConstMediaAction {
	override val argc = 4
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.caster !is ServerPlayerEntity)
			throw MishapBadCaster()

		val index = args.getInt(0, argc)
		val position = floatVector(reflectY(args.getVec3(1, argc)))
		val size = floatVector(args.getVec3(2, argc))
		val color = interpretColor(args.getVec3(3, argc))

		val scryglassState = (env.caster!! as PlayerEntityMinterface).getScryglassState()
		scryglassState.setIcon(index, RectIcon(position, size, color))
		return emptyList()
	}
}