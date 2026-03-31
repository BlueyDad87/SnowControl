package net.snowcontrol.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.snowcontrol.SnowControlMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Inject(
            method = "method_52370",
            at = @At("HEAD"),
            cancellable = true
    )
    private void snowcontrol$cancelInZone(BlockPos pos, CallbackInfo ci) {
        if (SnowControlMod.CONFIG.isBlocked(pos.getX(), pos.getZ())) {
            ci.cancel();
        }
    }
}