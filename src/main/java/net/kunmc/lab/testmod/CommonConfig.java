package net.kunmc.lab.testmod;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import static net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class CommonConfig {
    public static final CommonConfig instance;
    private static final ForgeConfigSpec spec;

    public final ConfigValue<Integer> intVal;
    public final ConfigValue<Boolean> boolVal;

    public CommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("commonConfig");

        this.intVal = builder.comment("This is integer value")
                .defineInRange("intVal", 50, 1, 100);

        this.boolVal = builder.comment("This is boolean value")
                .define("boolVal", true);

        builder.pop();
    }

    public static void register(ModLoadingContext ctx) {
        ctx.registerConfig(ModConfig.Type.COMMON, spec);
    }

    static {
        Pair<CommonConfig, ForgeConfigSpec> p = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        instance = p.getLeft();
        spec = p.getRight();
    }
}
