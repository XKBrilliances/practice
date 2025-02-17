package net.kaupenjoe.practicemod.datagen;

import net.kaupenjoe.practicemod.block.ModBlocks;
import net.kaupenjoe.practicemod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

import static net.kaupenjoe.practicemod.PracticeMod.MODID;
/**
 * 模组配方数据生成器
 * 负责生成合成、熔炼、高炉烧制等配方
 */
public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private  static final List<ItemLike> SAPPHIRE_SMELTABLES=List.of(ModItems.RAW_SAPPHIRE.get(),
            ModBlocks.SAPPHIRE_ORE.get(),//蓝水晶矿石
            ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get(),//深层蓝水晶矿石
            ModBlocks.NETHER_SAPPHIRE_ORE.get(),//下届蓝水晶矿石
            ModBlocks.END_STONE_SAPPHIRE_ORE.get());//末地蓝水晶矿石

    public ModRecipeProvider(PackOutput packOutput){
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        /* 高炉烧制配方生成 */
        oreBlasting(pWriter,
                SAPPHIRE_SMELTABLES, // 输入材料列表
                RecipeCategory.MISC, // 配方分类
                ModItems.SAPPHIRE.get(), // 输出物品
                0.25f, // 经验值
                100, // 烧制时间（刻）
                "sapphire"); // 配方组名（可能引起路径冲突）
        /* 熔炉熔炼配方生成 */
        oreSmelting(pWriter,
                SAPPHIRE_SMELTABLES, // 输入材料列表
                RecipeCategory.MISC, // 配方分类
                ModItems.SAPPHIRE.get(), // 输出物品
                0.25f, // 经验值
                200, // 熔炼时间（刻）
                "sapphire"); // 配方组名（可能引起路径冲突）

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SAPPHIRE_BLOCK.get())
                .pattern("###") // 第一行
                .pattern("###") // 第二行
                .pattern("###") // 第三行
                .define('#', ModItems.SAPPHIRE.get()) // 定义材料符号
                .unlockedBy(getHasName(ModItems.SAPPHIRE.get()), // 解锁条件名称
                        has(ModItems.SAPPHIRE.get())) // 需要拥有蓝宝石
                .save(pWriter, new ResourceLocation(MODID, "sapphire"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.SAPPHIRE.get(),9)
                .requires(ModBlocks.SAPPHIRE_BLOCK.get())// 需要蓝宝石块
                .unlockedBy(getHasName(ModBlocks.SAPPHIRE_BLOCK.get()),// 解锁条件名称
                        has(ModBlocks.SAPPHIRE_BLOCK.get()))// 需要拥有蓝宝石块
                .save(pWriter, new ResourceLocation(MODID, "sapphire_shapeless"));// 保存路径
    }
}
