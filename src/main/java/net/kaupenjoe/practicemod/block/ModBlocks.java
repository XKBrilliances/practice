package net.kaupenjoe.practicemod.block;

import net.kaupenjoe.practicemod.PracticeMod;
import net.kaupenjoe.practicemod.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * 适用于Minecraft 1.20.1 Forge的方块注册类
 * 主要功能：
 * 1. 注册自定义方块
 * 2. 自动注册对应的方块物品
 * 3. 配置方块属性
 */
public class ModBlocks {
    // 创建方块延迟注册器（Forge 1.20.1专用方式)
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PracticeMod.MODID);
    // 注册蓝宝石方块（核心注册逻辑）
    public static final RegistryObject<Block> SAPPHIRE_BLOCK = registerBlock("sapphire_block",// 注册名称（自动转为snake_case格式）
            () -> new Block(BlockBehaviour.Properties// 1.20.1的BlockBehaviour.Properties配置链
                    .of()
                    .strength(5.0f,6.0f) // 硬度和爆炸抗性
                    .sound(SoundType.AMETHYST)//紫水晶音效
                    // 可添加更多属性：
                    // .strength(5.0f, 6.0f) // 硬度和爆炸抗性
                    // .requiresCorrectToolForDrops() // 需要正确工具采集
            ));
    //粗蓝水晶矿
    public static final RegistryObject<Block> RAW_SAPPHIRE_BLOCK = registerBlock("raw_sapphire_block",// 注册名称（自动转为snake_case格式）
            () -> new Block(BlockBehaviour.Properties// 1.20.1的BlockBehaviour.Properties配置链
                    .of()
                    .strength(5.0f,6.0f) // 硬度和爆炸抗性
                    .sound(SoundType.AMETHYST)//紫水晶音效
                    // 可添加更多属性：
                    // .strength(5.0f, 6.0f) // 硬度和爆炸抗性
                    // .requiresCorrectToolForDrops() // 需要正确工具采集
            ));
    //蓝水晶矿石
    public static final RegistryObject<Block> SAPPHIRE_ORE = registerBlock("sapphire_ore",// 注册名称（自动转为snake_case格式）
            () -> new Block(BlockBehaviour.Properties// 1.20.1的BlockBehaviour.Properties配置链
                    .of()
                    .strength(5.0f,6.0f) // 硬度和爆炸抗性
                    .sound(SoundType.AMETHYST)
                    // 可添加更多属性：
                    // .strength(5.0f, 6.0f) // 硬度和爆炸抗性
                    // .requiresCorrectToolForDrops() // 需要正确工具采集
            ));
    //深层蓝水晶矿石
    public static final RegistryObject<Block> DEEPSLATE_SAPPHIRE_ORE = registerBlock("deepslate_sapphire_ore",// 注册名称（自动转为snake_case格式）
            () -> new Block(BlockBehaviour.Properties// 1.20.1的BlockBehaviour.Properties配置链
                    .of()
                    .strength(5.0f,6.0f) // 硬度和爆炸抗性
                    .sound(SoundType.AMETHYST)
                    // 可添加更多属性：
                    // .strength(5.0f, 6.0f) // 硬度和爆炸抗性
                    // .requiresCorrectToolForDrops() // 需要正确工具采集
            ));
    //末地蓝水晶矿石
    public static final RegistryObject<Block> END_STONE_SAPPHIRE_ORE = registerBlock("end_stone_sapphire_ore",// 注册名称（自动转为snake_case格式）
            () -> new Block(BlockBehaviour.Properties// 1.20.1的BlockBehaviour.Properties配置链
                    .of()
                    .strength(5.0f,6.0f) // 硬度和爆炸抗性
                    .sound(SoundType.AMETHYST)
                    // 可添加更多属性：
                    // .strength(5.0f, 6.0f) // 硬度和爆炸抗性
                    // .requiresCorrectToolForDrops() // 需要正确工具采集
            ));
    //下届蓝水晶矿石
    public static final RegistryObject<Block> NETHER_SAPPHIRE_ORE = registerBlock("nether_sapphire_ore",// 注册名称（自动转为snake_case格式）
            () -> new Block(BlockBehaviour.Properties// 1.20.1的BlockBehaviour.Properties配置链
                    .of()
                    .strength(5.0f,6.0f) // 硬度和爆炸抗性
                    .sound(SoundType.AMETHYST)
                    // 可添加更多属性：
                    // .strength(5.0f, 6.0f) // 硬度和爆炸抗性
                    // .requiresCorrectToolForDrops() // 需要正确工具采集
            ));
// 可扩展属性：
    // .stacksTo(64) // 堆叠数量
    // .rarity(Rarity.RARE) // 稀有度
    /**
     * 通用方块注册方法（1.20.1推荐方式）
     * @param name 注册名称（自动转为下划线格式）
     * @param block 方块供应商（lambda表达式）
     * @return RegistryObject<Block> 注册对象
     */
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    /**
     * 自动注册方块对应的物品形式（1.20.1特性）
     * @param name 物品名称（需与方块名称一致）
     * @param block 已注册的方块对象
     */
    private static <T extends Block> void registerBlockItem (String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(
                block.get(),
                new Item.Properties()));// 获取已注册的方块实例
    }

    public static final void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
