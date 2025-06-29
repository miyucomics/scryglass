package miyucomics.scryglass.actions.alter

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.scryglass.state.PlayerEntityMinterface
import net.minecraft.server.network.ServerPlayerEntity

class OpScaleIcon : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.caster !is ServerPlayerEntity)
			throw MishapBadCaster()
		val scryglassState = (env.caster!! as PlayerEntityMinterface).getScryglassState()
		scryglassState.get(args.getInt(0, argc))?.scale = args.getDouble(1, argc).toFloat()
		return emptyList()
	}
}