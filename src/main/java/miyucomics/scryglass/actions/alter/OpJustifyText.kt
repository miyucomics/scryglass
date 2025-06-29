package miyucomics.scryglass.actions.alter

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import miyucomics.scryglass.icons.TextIcon
import miyucomics.scryglass.icons.TextJustification
import miyucomics.scryglass.state.PlayerEntityMinterface
import net.minecraft.server.network.ServerPlayerEntity

class OpJustifyText : ConstMediaAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		if (env.caster !is ServerPlayerEntity)
			throw MishapBadCaster()
		val scryglassState = (env.caster!! as PlayerEntityMinterface).getScryglassState()
		val index = args.getInt(0, argc)
		val icon = scryglassState.get(index)
		if (icon is TextIcon)
			scryglassState.setIcon(index, TextIcon(icon.text, icon.position, enumValues<TextJustification>()[args.getPositiveIntUnderInclusive(1, 2, argc)]))
		return emptyList()
	}
}