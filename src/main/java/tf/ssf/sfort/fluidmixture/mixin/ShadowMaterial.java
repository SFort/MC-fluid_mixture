package tf.ssf.sfort.fluidmixture.mixin;

import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.block.Material.class)
public class ShadowMaterial {
    @Final
    @Shadow
    public static final net.minecraft.block.Material WATER= new net.minecraft.block.Material.Builder(MaterialColor.WATER).allowsMovement().notSolid().replaceable().liquid().build();
    @Final
    @Shadow
    public static final net.minecraft.block.Material LAVA= new net.minecraft.block.Material.Builder(MaterialColor.WATER).allowsMovement().notSolid().replaceable().liquid().build();
}