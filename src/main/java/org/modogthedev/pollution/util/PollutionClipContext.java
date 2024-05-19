package org.modogthedev.pollution.util;

import com.simibubi.create.AllTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PollutionClipContext extends ClipContext {
    //Todo, remove these 3? i dont see the use - cake @modog
    private final Vec3 from;
    private final Vec3 to;
    private final Fluid fluid;
    private final PollutionCollides block;
    private final CollisionContext collisionContext;

    public PollutionClipContext(Vec3 from, Vec3 to, PollutionCollides block, Fluid fluid, @Nullable Entity entity, ClipContext.Block block1) {
        super(from, to, block1, fluid, entity);
        this.from = from;
        this.to = to;
        this.block = block;
        this.fluid = fluid;
        this.collisionContext = entity == null ? CollisionContext.empty() : CollisionContext.of(entity);
    }

    @Override
    public VoxelShape getBlockShape(BlockState blockState, BlockGetter level, BlockPos pos) {
        return this.block.get(blockState, level, pos, this.collisionContext);
    }

    public enum PollutionCollides implements ShapeGetter {

        POLLUTION_COLLIDES((state, world, pos, context) ->
                state.getBlock()
                        instanceof LeavesBlock ||
                        AllTags.AllBlockTags.FAN_TRANSPARENT.matches(state) ||
                        state.is(Blocks.AIR) ? Shapes.empty() : Shapes.block());

        private final ShapeGetter shapeGetter;

        PollutionCollides(ShapeGetter shapeGetter) {
            this.shapeGetter = shapeGetter;
        }

        public VoxelShape get(BlockState arg, BlockGetter arg2, BlockPos arg3, CollisionContext arg4) {
            return this.shapeGetter.get(arg, arg2, arg3, arg4);
        }
    }

}