package miyucomics.scryglass.state

import miyucomics.scryglass.ScryglassMain
import miyucomics.scryglass.viewforms.TextViewform
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.SimpleRegistry

object ViewformData {
	private val VIEWFORM_REGISTRY_KEY: RegistryKey<Registry<ViewformType<out Viewform>>> = RegistryKey.ofRegistry(ScryglassMain.id("viewform_type"))
	val VIEWFORM_REGISTRY: SimpleRegistry<ViewformType<out Viewform>> = FabricRegistryBuilder.createSimple(VIEWFORM_REGISTRY_KEY).attribute(RegistryAttribute.MODDED).buildAndRegister()

	fun init() {
		Registry.register(VIEWFORM_REGISTRY, ScryglassMain.id("text"), TextViewform.TYPE)
	}
}