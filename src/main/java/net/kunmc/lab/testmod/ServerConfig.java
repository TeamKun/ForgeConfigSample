package net.kunmc.lab.testmod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import static net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ServerConfig {
    public static final ServerConfig instance;
    private static final ForgeConfigSpec spec;

    public final ConfigValue<Integer> intVal;
    public final ConfigValue<Boolean> boolVal;

    public ServerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("serverConfig");

        this.intVal = builder.comment("This is integer value")
                .defineInRange("intVal", 50, 1, 100);

        this.boolVal = builder.comment("This is boolean value")
                .define("boolVal", true);

        builder.pop();
    }

    public static void register(ModLoadingContext ctx) {
        ctx.registerConfig(ModConfig.Type.SERVER, spec);
    }

    static {
        Pair<ServerConfig, ForgeConfigSpec> p = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        instance = p.getLeft();
        spec = p.getRight();
    }
}
