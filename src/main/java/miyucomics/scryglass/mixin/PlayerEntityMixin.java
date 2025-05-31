package miyucomics.scryglass.mixin;

import kotlin.Pair;
import miyucomics.scryglass.PlayerEntityMinterface;
import miyucomics.scryglass.state.ViewformState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityMinterface {
	@Unique
	private Pair<@NotNull Double, @NotNull Double> windowSize = new Pair<>(0.0, 0.0);
	@Unique
	private @NotNull ViewformState viewformState = new ViewformState();

	@Override
	public @NotNull Pair<@NotNull Double, @NotNull Double> getWindowSize() {
		return windowSize;
	}

	@Override
	public void setWindowSize(@NotNull Pair<@NotNull Double, @NotNull Double> size) {
		windowSize = size;
	}

	@Override
	public @NotNull ViewformState getViewformState() {
		return viewformState;
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void updateClient(CallbackInfo ci) {
		if (!((Entity) (Object) this).getWorld().isClient)
			viewformState.updateIfNeeded((ServerPlayerEntity) (Object) this);
	}

	@Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
	public void saveData(NbtCompound nbtCompound, CallbackInfo ci) {
		nbtCompound.put("viewforms", viewformState.serialize());
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
	public void readData(NbtCompound nbtCompound, CallbackInfo ci) {
		viewformState = ViewformState.deserialize(nbtCompound.getCompound("viewforms"));
	}
}