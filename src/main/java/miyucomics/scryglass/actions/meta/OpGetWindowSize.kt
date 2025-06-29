package miyucomics.scryglass.actions.meta

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.scryglass.state.PlayerEntityMinterface
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Vec3d

class OpGetWindowSize : ConstMediaAction {
	override val argc = 0
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.caster !is ServerPlayerEntity)
			throw MishapBadCaster()
		val window = (env.caster!! as PlayerEntityMinterface).getWindowSize()
		return Vec3d(window.first, window.second, 0.0).asActionResult
	}
}