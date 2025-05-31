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

class OpDrawText : ConstMediaAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.caster !is ServerPlayerEntity)
			throw MishapBadCaster()

		val index = args.getInt(0, argc)
		val position = args.getVec3(1, argc)
		val text = args[2].display()

		val viewformState = (env.caster!! as PlayerEntityMinterface).getViewformState()
		viewformState.setViewform(index, TextViewform(text, position))
		return emptyList()
	}
}