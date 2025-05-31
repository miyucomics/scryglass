package miyucomics.scryglass.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.scryglass.PlayerEntityMinterface
import miyucomics.scryglass.state.ViewformState
import miyucomics.scryglass.viewforms.TextViewform
import net.minecraft.server.network.ServerPlayerEntity

class OpRemoveViewform : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.caster !is ServerPlayerEntity)
			throw MishapBadCaster()
		val viewformState = (env.caster!! as PlayerEntityMinterface).getViewformState()
		viewformState.removeViewform(args.getInt(0, argc))
		return emptyList()
	}
}