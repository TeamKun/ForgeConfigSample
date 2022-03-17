package net.kunmc.lab.testmod;

import net.minecraft.item.ItemTier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import static net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class ClientConfig {
    public static final ClientConfig instance;
    private static final ForgeConfigSpec spec;

    public final ConfigValue<Double> doubleVal;
    public final ConfigValue<ItemTier> itemTierVal;

    public ClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("clientConfig");

        this.doubleVal = builder.comment("This is double value.")
                .defineInRange("doubleVal", 50.0, 1.0, 100.0);

        this.itemTierVal = builder.comment("This is ItemTier Enum value.")
                .defineEnum("materialVal", ItemTier.IRON);

        builder.pop();
    }

    public static void register(ModLoadingContext ctx) {
        ctx.registerConfig(ModConfig.Type.CLIENT, spec);
    }

    static {
        Pair<ClientConfig, ForgeConfigSpec> p = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        instance = p.getLeft();
        spec = p.getRight();
    }
}
