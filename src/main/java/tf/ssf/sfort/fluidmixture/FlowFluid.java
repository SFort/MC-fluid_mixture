package tf.ssf.sfort.fluidmixture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.fluid.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;

//TODO maybe overwrite bucketitem.java to never waste fluid
public class FlowFluid {
    public static void flow(WorldAccess world, BlockPos fluidPos, FluidState state) {
        if(world.isClient()){return;}
        boolean isWater = state.getFluid().matchesType(Fluids.WATER);
        boolean thisblock = world.getBlockState(fluidPos).getBlock() instanceof FluidFillable;
        BlockState downblock = world.getBlockState(fluidPos.down());
        //TODO empty waterlogged block
        //TODO waterlog doors
        if (downblock.getBlock() instanceof FluidFillable) {
            if (!downblock.getProperties().contains(Properties.WATERLOGGED)){return;}
              if (!downblock.get(Properties.WATERLOGGED) && isWater) {
                    if (thisblock) {
                        world.setBlockState(fluidPos, world.getBlockState(fluidPos).with(Properties.WATERLOGGED, false), 3);
                    }
                    else{
                        world.setBlockState(fluidPos, Blocks.AIR.getDefaultState(), 3);
                    }
                    world.setBlockState(fluidPos.down(), downblock.with(Properties.WATERLOGGED, true), 3);
                  world.getFluidTickScheduler().schedule(fluidPos.down(), Fluids.WATER, 15);
                }
        }
        if (thisblock){
            return;
        }
        int downlvl = downblock.getFluidState().getLevel();
        int thislvl = world.getBlockState(fluidPos).getFluidState().getLevel();
        if (downlvl != 8 && downblock.canBucketPlace(state.getFluid())) {
            setLevel(downlvl+thislvl-8, fluidPos,world, state);
            setLevel(downlvl+thislvl, fluidPos.down(),world, state);
        } else {
            int i =0;
            for (Direction side : Direction.Type.HORIZONTAL) {
                BlockState sideblock = world.getBlockState(fluidPos.offset(side));
                if (sideblock.canBucketPlace(state.getFluid())){
                    sideblock.getBlock();
                    int sidelvl =sideblock.getFluidState().getLevel();
                    if (sidelvl ==8){i++;}
                    if (thislvl-1 > sidelvl){
                        setLevel(thislvl-1, fluidPos,world,state);
                        thislvl=-1;
                        setLevel(sidelvl+1, fluidPos.offset(side),world,state);
                        Block.dropStacks(sideblock,world.getWorld(), fluidPos.offset(side));
                    }

                }
            }

            if (i>1 && isWater){
                Biome.Category inBiome = world.getBiome(fluidPos).getCategory();
                if (inBiome == Biome.Category.OCEAN
                        ||inBiome == Biome.Category.BEACH
                        || inBiome == Biome.Category.RIVER){
                    //NOTE this does not generate water sources if on solid blocks it's a feature
                    world.setBlockState(fluidPos, Blocks.WATER.getDefaultState(), 3);
                }
            }
        }
    }

    public static void setLevel(int level, BlockPos pos, WorldAccess world, FluidState fluid) {
        if (level >= 8) {
            world.setBlockState(pos, fluid.getFluid().getDefaultState().getBlockState(), 3);
        } else if (level <= 0) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        } else {
            world.setBlockState(pos, ((FlowableFluid) fluid.getFluid()).getFlowing(level, false).getBlockState(), 3);
        }
    }
}
