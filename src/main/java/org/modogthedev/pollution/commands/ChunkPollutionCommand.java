package org.modogthedev.pollution.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.modogthedev.pollution.system.worldPollution.ModWorldPollution;

public class ChunkPollutionCommand {
    public ChunkPollutionCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("pollution").requires((p_138606_) -> {
            return p_138606_.hasPermission(2);
        }).then(Commands.literal("set").then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("count", IntegerArgumentType.integer(1)).executes((commands) -> {
            return setPollution(commands.getSource(), BlockPosArgument.getLoadedBlockPos(commands, "pos"), IntegerArgumentType.getInteger(commands, "count"));
        })))).then(Commands.literal("get").then(Commands.argument("pos", BlockPosArgument.blockPos()).executes((commands) -> {
            return getPollution(commands.getSource(),BlockPosArgument.getLoadedBlockPos(commands, "pos"));
        }))));
    }

    private int setPollution(CommandSourceStack source, BlockPos pos, Integer amount) throws CommandSyntaxException {
        ServerLevel serverlevel = source.getLevel();
        Integer present = ModWorldPollution.get(serverlevel).getPollution(pos);
        ModWorldPollution.get(serverlevel).changePollution(pos, amount-present);
        source.sendSuccess(Component.translatable("commands.pollution.success",pos.getX(),pos.getY(),pos.getZ(),amount),true);
        return 1;
    }
    private int getPollution(CommandSourceStack source, BlockPos pos) throws CommandSyntaxException {
        Integer present = ModWorldPollution.get(source.getLevel()).getPollution(pos);
        source.sendSuccess(Component.translatable("commands.pollution.get",present),true);
        return 1;
    }
}
