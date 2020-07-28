package tf.ssf.sfort.fluidmixture.mixin;

import net.minecraft.fluid.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tf.ssf.sfort.fluidmixture.FlowWater;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import org.spongepowered.asm.mixin.injection.At;

@Mixin(net.minecraft.fluid.FlowableFluid.class)
public class Flow {

    @Inject(at = @At("HEAD"), method = "onScheduledTick", cancellable = true)
    private void onScheduledTick(World world, BlockPos pos, FluidState state, CallbackInfo callback) {
        FlowWater.flow(world, pos, state);
        callback.cancel();
    }
    @Inject(at = @At("HEAD"), method = "getUpdatedState", cancellable = true)
    private void getUpdatedState(WorldView world, BlockPos pos, BlockState state, CallbackInfoReturnable<FluidState> callback) {
        callback.setReturnValue(Fluids.FLOWING_WATER.getFlowing(state.getFluidState().getLevel(), false));
    }
}