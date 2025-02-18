package net.kaupenjoe.practicemod.datagen.loot;

import net.kaupenjoe.practicemod.block.ModBlocks;
import net.kaupenjoe.practicemod.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
// 定义一个名为 ModBlockLootTables 的公共类，继承自 BlockLootSubProvider
public class ModBlockLootTables extends BlockLootSubProvider{

    public ModBlockLootTables() {// 构造函数
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());// 调用父类构造函数，设置掉落表和特性标志
    }

    @Override
    protected void generate() {// 生成函数，用于生成掉落表
        this.dropSelf(ModBlocks.SAPPHIRE_BLOCK.get());// 掉落蓝宝石方块本身
        this.dropSelf(ModBlocks.RAW_SAPPHIRE_BLOCK.get()); // 掉落粗蓝宝石方块本身

        this.add(ModBlocks.SAPPHIRE_ORE.get(),// 为蓝宝石矿石添加掉落表
                block -> createCopperLikeOerDrops(ModBlocks.SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get()));// 创建铜矿石掉落蓝宝石矿石的掉落表
        this.add(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(),
                block -> createCopperLikeOerDrops(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get())); // 为深层蓝宝石矿石添加掉落表
        this.add(ModBlocks.NETHER_SAPPHIRE_ORE.get(),
                block -> createCopperLikeOerDrops(ModBlocks.NETHER_SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get())); // 为下界蓝宝石矿石添加掉落表
        this.add(ModBlocks.END_STONE_SAPPHIRE_ORE.get(),
                block -> createCopperLikeOerDrops(ModBlocks.END_STONE_SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get()));// 为末地石蓝宝石矿石添加掉落表
    }
    protected LootTable.Builder createCopperLikeOerDrops(Block pBlock,Item item) {// 创建铜矿石掉落表的方法
        return  createSilkTouchOrShearsDispatchTable(pBlock,// 创建丝绸触碰或剪刀掉落表
                this.applyExplosionDecay(pBlock,// 应用爆炸衰减量
                        LootItem.lootTableItem(item)// 掉落物品
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))// 设置掉落数量
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));// 应用额外掉落计数
    }
    @Override
    protected Iterable<Block> getKnownBlocks() { // 获取已知方块的方法
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}

