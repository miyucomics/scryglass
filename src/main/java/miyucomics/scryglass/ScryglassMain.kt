package miyucomics.scryglass

import miyucomics.scryglass.icons.*
import miyucomics.scryglass.state.PlayerEntityMinterface
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry
import net.minecraft.util.Identifier
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f

class ScryglassMain : ModInitializer {
	override fun onInitialize() {
		ScryglassActions.init()

		Registry.register(ICON_REGISTRY, id("line"), LineIcon.TYPE)
		Registry.register(ICON_REGISTRY, id("text"), TextIcon.TYPE)
		Registry.register(ICON_REGISTRY, id("rect"), RectIcon.TYPE)

		ServerPlayNetworking.registerGlobalReceiver(DIMENSIONS_CHANNEL) { _, player, _, buf, _ ->
			(player as PlayerEntityMinterface).setWindowSize(Pair(buf.readInt().toDouble(), buf.readInt().toDouble()))
			(player as PlayerEntityMinterface).getScryglassState().prime(player)
		}
	}

	companion object {
		fun id(string: String) = Identifier("scryglass", string)
		val DIMENSIONS_CHANNEL = id("dimensions")
		val PRIMER_CHANNEL = id("full_sync")
		val UPDATE_CHANNEL = id("update")

		private val ICON_REGISTRY_KEY: RegistryKey<Registry<IconType<out Icon>>> = RegistryKey.ofRegistry(id("icon_type"))
		val ICON_REGISTRY: SimpleRegistry<IconType<out Icon>> = FabricRegistryBuilder.createSimple(ICON_REGISTRY_KEY).attribute(RegistryAttribute.MODDED).buildAndRegister()

		fun reflectY(vec: Vec3d) = Vec3d(vec.x, -vec.y, vec.z)
		fun floatVector(vec: Vec3d) = Vector3f(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())
		fun interpretColor(vec: Vec3d) = ColorHelper.Argb.getArgb(255, (vec.x * 255).toInt(), (vec.y * 255).toInt(), (vec.z * 255).toInt())
	}
}