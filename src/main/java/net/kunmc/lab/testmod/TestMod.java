package net.kunmc.lab.testmod;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.ItemTier;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.EnumArgument;

import java.util.function.Consumer;

@Mod("testmod")
public class TestMod {
    public TestMod() {
        MinecraftForge.EVENT_BUS.register(this);

        CommonConfig.register(ModLoadingContext.get());
        ClientConfig.register(ModLoadingContext.get());
        ServerConfig.register(ModLoadingContext.get());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent e) {
        e.getDispatcher().register(Commands.literal("checkConfig")
                .executes(ctx -> {
                    sendMessage(ctx, "---Common---");
                    sendMessage(ctx, "intVal", CommonConfig.instance.intVal.get());
                    sendMessage(ctx, "boolVal", CommonConfig.instance.boolVal.get());

                    sendMessage(ctx, "---Client---");
                    sendMessage(ctx, "doubleVal", ClientConfig.instance.doubleVal.get());
                    sendMessage(ctx, "itemTierVal", ClientConfig.instance.itemTierVal.get());

                    sendMessage(ctx, "---Server---");
                    sendMessage(ctx, "intVal", ServerConfig.instance.intVal.get());
                    sendMessage(ctx, "boolVal", ServerConfig.instance.boolVal.get());

                    return 1;
                })
        );

        e.getDispatcher().register(Commands.literal("changeConfig")
                .then(Commands.literal("common")
                        .then(createConfigEntryNode("intVal", IntegerArgumentType.integer(), ctx -> {
                            CommonConfig.instance.intVal.set(IntegerArgumentType.getInteger(ctx, "intVal"));
                        }))
                        .then(createConfigEntryNode("boolVal", BoolArgumentType.bool(), ctx -> {
                            CommonConfig.instance.boolVal.set(BoolArgumentType.getBool(ctx, "boolVal"));
                        }))
                )
                .then(Commands.literal("client")
                        .then(createConfigEntryNode("doubleVal", DoubleArgumentType.doubleArg(), ctx -> {
                            ClientConfig.instance.doubleVal.set(DoubleArgumentType.getDouble(ctx, "doubleVal"));
                        }))
                        .then(createConfigEntryNode("itemTierVal", EnumArgument.enumArgument(ItemTier.class), ctx -> {
                            ClientConfig.instance.itemTierVal.set(ctx.getArgument("itemTierVal", ItemTier.class));
                        }))
                )
                .then(Commands.literal("server")
                        .then(createConfigEntryNode("intVal", IntegerArgumentType.integer(), ctx -> {
                            ServerConfig.instance.intVal.set(IntegerArgumentType.getInteger(ctx, "intVal"));
                        }))
                        .then(createConfigEntryNode("boolVal", BoolArgumentType.bool(), ctx -> {
                            ServerConfig.instance.boolVal.set(BoolArgumentType.getBool(ctx, "boolVal"));
                        }))
                )
        );
    }

    private LiteralArgumentBuilder<CommandSource> createConfigEntryNode(String name, ArgumentType<?> type, Consumer<CommandContext<CommandSource>> execute) {
        return Commands.literal(name)
                .then(Commands.argument(name, type)
                        .executes(ctx -> {
                            execute.accept(ctx);
                            sendMessage(ctx, "changed");

                            return 1;
                        }));
    }

    private void sendMessage(CommandContext<CommandSource> ctx, String name, Object value) {
        sendMessage(ctx, name + ": " + value.toString());
    }

    private void sendMessage(CommandContext<CommandSource> ctx, String msg) {
        ctx.getSource().sendFeedback(new StringTextComponent(msg), true);
    }
}
