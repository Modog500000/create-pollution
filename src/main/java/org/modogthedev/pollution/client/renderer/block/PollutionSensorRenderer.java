package org.modogthedev.pollution.client.renderer.block;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.gauge.GaugeBlock;
import com.simibubi.create.content.kinetics.gauge.GaugeBlockEntity;
import com.simibubi.create.content.kinetics.gauge.GaugeRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.modogthedev.pollution.block.PollutionSensor;
import org.modogthedev.pollution.block.blockentity.PollutionSensorBlockEntity;

public class PollutionSensorRenderer implements BlockEntityRenderer<PollutionSensorBlockEntity> {
    protected GaugeBlock.Type type = GaugeBlock.Type.SPEED;

    public PollutionSensorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PollutionSensorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                       int light, int overlay) {

        BlockState gaugeState = be.getBlockState();

        PartialModel partialModel = (type == GaugeBlock.Type.SPEED ? AllPartialModels.GAUGE_HEAD_SPEED : AllPartialModels.GAUGE_HEAD_STRESS);
        SuperByteBuffer headBuffer =
                CachedBufferer.partial(partialModel, gaugeState);
        SuperByteBuffer dialBuffer = CachedBufferer.partial(AllPartialModels.GAUGE_DIAL, gaugeState);

        float dialPivot = 5.75f / 16;
        float progress = Mth.lerp(partialTicks, be.prevDialState, be.dialState);


        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        rotateBufferTowards(dialBuffer, be.getBlockState().getValue(PollutionSensor.FACING)).translate(0, dialPivot, dialPivot)
                .rotate(Direction.EAST, (float) (Math.PI / 2 * -progress))
                .translate(0, -dialPivot, -dialPivot)
                .light(light)
                .renderInto(ms, vb);
        rotateBufferTowards(headBuffer, be.getBlockState().getValue(PollutionSensor.FACING)).light(light)
                .renderInto(ms, vb);

    }

    protected SuperByteBuffer rotateBufferTowards(SuperByteBuffer buffer, Direction target) {
        return buffer.rotateCentered(Direction.UP, (float) ((-target.toYRot() - 90) / 180 * Math.PI));
    }
}
