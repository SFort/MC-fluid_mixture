package tf.ssf.sfort.fluidmixture.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBlock.class)
public class FluidPiston {
    @Inject(at = @At("HEAD"), method = "isMovable", cancellable = true)
    private static void isMovable(BlockState state,
                                  World world,
                                  BlockPos pos,
                                  Direction motionDir,
                                  boolean canBreak,
                                  Direction pistonDir,
                                  CallbackInfoReturnable<Boolean> callback) {
        if (!world.getFluidState(pos.offset(motionDir.getOpposite())).isEmpty() &&
                state.getFluidState().isEmpty()) {
            callback.setReturnValue(false);
        }
    }
}