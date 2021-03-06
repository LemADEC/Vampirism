package de.teamlapen.vampirism.core;

import de.teamlapen.vampirism.VampirismMod;
import de.teamlapen.vampirism.api.items.IItemWithTier;
import de.teamlapen.vampirism.config.Configs;
import de.teamlapen.vampirism.util.SRGNAMES;
import de.teamlapen.vampirism.world.gen.village.VillagePieceModChurch;
import de.teamlapen.vampirism.world.gen.village.VillagePieceTrainer;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;

import static de.teamlapen.lib.lib.util.UtilLib.getNull;

/**
 * Handles Village related stuff
 */
public class ModVillages {
    @GameRegistry.ObjectHolder("vampirism:vampire_expert")
    public static final VillagerRegistry.VillagerProfession profession_vampire_expert = getNull();
    private final static String TAG = "ModVillages";

    static void init() {
        registerCreationHandlers();
        registerPieces();
        registerTrades();
    }

    private static void registerPieces() {
        MapGenStructureIO.registerStructureComponent(VillagePieceTrainer.class, "Vampirism-TR");
        MapGenStructureIO.registerStructureComponent(VillagePieceModChurch.class, "Vampirism-MC");
    }

    private static void registerCreationHandlers() {
        VillagerRegistry.instance().registerVillageCreationHandler(new VillagePieceTrainer.CreationHandler());
        VillagerRegistry.instance().registerVillageCreationHandler(new VillagePieceModChurch.CreationHandler());
    }

    public static void modifyVillageSize(MapGenBase mapGenVillage) {
        if (mapGenVillage instanceof MapGenVillage) {


            try {
                ReflectionHelper.setPrivateValue(MapGenVillage.class, (MapGenVillage) mapGenVillage, Configs.village_size, "size", SRGNAMES.MapGenVillage_size);
            } catch (ReflectionHelper.UnableToAccessFieldException e) {
                VampirismMod.log.e(TAG, e, "Could not modify field 'terrainType' in MapGenVillage");
            }

            try {
                ReflectionHelper.setPrivateValue(MapGenVillage.class, (MapGenVillage) mapGenVillage, Configs.village_density, "distance", SRGNAMES.MapGenVillage_distance);
            } catch (ReflectionHelper.UnableToAccessFieldException e) {
                VampirismMod.log.e(TAG, e, "Could not modify field for village density in MapGenVillage");
            }
            try {
                ReflectionHelper.setPrivateValue(MapGenVillage.class, (MapGenVillage) mapGenVillage, Configs.village_min_dist, "minTownSeparation", SRGNAMES.MapGenVillage_minTownSeperation);
            } catch (ReflectionHelper.UnableToAccessFieldException e) {
                VampirismMod.log.e(TAG, e, "Could not modify field for village min dist in MapGenVillage");
            }


            VampirismMod.log.d(TAG, "Modified MapGenVillage fields.");

        } else {
            //Should not be possible
            VampirismMod.log.e(TAG, "VillageGen (%s) is not an instance of MapGenVillage, can't modify gen", mapGenVillage);
        }
    }

    private static void registerTrades() {
        VillagerRegistry.VillagerProfession priest = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("minecraft", "priest"));
        if (priest != null) {
            VillagerRegistry.VillagerCareer hunterPriest = new VillagerRegistry.VillagerCareer(priest, "vampirism.hunter_priest");
            hunterPriest.addTrade(1, new EntityVillager.EmeraldForItems(Items.GOLD_INGOT, new EntityVillager.PriceInfo(8, 10)));
            hunterPriest.addTrade(2, new EntityVillager.ListItemForEmeralds(ModItems.holy_water_bottle, new EntityVillager.PriceInfo(-8, -2)));
            hunterPriest.addTrade(3, new EntityVillager.ListItemForEmeralds(ModItems.holy_water_bottle.setTier(new ItemStack(ModItems.holy_water_bottle), IItemWithTier.TIER.ENHANCED), new EntityVillager.PriceInfo(-5, -1)));
            hunterPriest.addTrade(3, new EntityVillager.ListItemForEmeralds(ModItems.holy_salt, new EntityVillager.PriceInfo(-10, -3)));
            hunterPriest.addTrade(4, new EntityVillager.ListItemForEmeralds(Items.EXPERIENCE_BOTTLE, new EntityVillager.PriceInfo(3, 11)));
        } else {
            VampirismMod.log.w(TAG, "Did not find vanilla priest profession");
        }
        VillagerRegistry.VillagerCareer normal_vampire_expert = new VillagerRegistry.VillagerCareer(profession_vampire_expert, "vampirism.vampire_expert");
        normal_vampire_expert.addTrade(1, new EntityVillager.EmeraldForItems(ModItems.vampire_fang, new EntityVillager.PriceInfo(20, 30)));
        normal_vampire_expert.addTrade(2, new EntityVillager.EmeraldForItems(ModItems.vampire_book, new EntityVillager.PriceInfo(1, 1)));
    }

    static void registerProfessions(IForgeRegistry<VillagerRegistry.VillagerProfession> registry) {
        registry.register(new VillagerRegistry.VillagerProfession("vampirism:vampire_expert", "vampirism:textures/entity/villager_vampire_expert.png",
                "minecraft:textures/entity/zombie_villager/zombie_farmer.png"));
    }
}
