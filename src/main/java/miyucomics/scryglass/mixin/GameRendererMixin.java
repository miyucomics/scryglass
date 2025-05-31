package miyucomics.scryglass.mixin;

import miyucomics.scryglass.inits.ScryglassNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
	@Inject(method = "onResized", at = @At("TAIL"))
	private void notifyAboutResize(int width, int height, CallbackInfo ci) {
		if (MinecraftClient.getInstance().world != null)
			ScryglassNetworking.sendDimensions();
	}
}