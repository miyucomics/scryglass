package miyucomics.scryglass.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.scryglass.state.PlayerEntityMinterface
import net.minecraft.server.network.ServerPlayerEntity

class OpFlip : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.caster !is ServerPlayerEntity)
			throw MishapBadCaster()
		(env.caster!! as PlayerEntityMinterface).getScryglassState().flip()
		return emptyList()
	}
}