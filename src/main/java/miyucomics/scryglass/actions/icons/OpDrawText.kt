package miyucomics.scryglass.actions.icons

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.scryglass.ScryglassMain.Companion.floatVector
import miyucomics.scryglass.ScryglassMain.Companion.reflectY
import miyucomics.scryglass.icons.TextIcon
import miyucomics.scryglass.icons.TextJustification
import miyucomics.scryglass.state.PlayerEntityMinterface
import net.minecraft.server.network.ServerPlayerEntity

class OpDrawText : ConstMediaAction {
	override val argc = 4
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.caster !is ServerPlayerEntity)
			throw MishapBadCaster()

		val index = args.getInt(0, argc)
		val position = reflectY(args.getVec3(1, argc))
		val justify = args.getPositiveIntUnderInclusive(2, 2, argc)
		val text = args[3].display()

		val scryglassState = (env.caster!! as PlayerEntityMinterface).getScryglassState()
		scryglassState.setIcon(index, TextIcon(text, floatVector(position), enumValues<TextJustification>()[justify]))
		return emptyList()
	}
}