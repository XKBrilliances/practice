package net.kaupenjoe.practicemod.item;

import net.kaupenjoe.practicemod.PracticeMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems { // 注意：类名建议改为大驼峰式命名，如 ModItems
    // 创建一个延迟注册器，用于管理物品注册
    // ITEMS 是注册Item类型的实例，使用模组ID作为命名空间
    public  static final DeferredRegister<Item> ITEMS =
           DeferredRegister.create(ForgeRegistries.ITEMS, PracticeMod.MODID);
    // 注册"蓝宝石"物品
    // RegistryObject<Item> 表示一个已注册物品的引用
    public static final RegistryObject<Item> SAPPHIRE = ITEMS.register("sapphire",
            ()->new Item(new Item.Properties()));// 使用默认物品属性
    // 注册"原始蓝宝石"物品
    public static final RegistryObject<Item> RAW_SAPPHIRE = ITEMS.register("raw_sapphire",
            ()->new Item(new Item.Properties()
                    .stacksTo(64)// 创造中键堆叠数量
                    .rarity(Rarity.RARE)));// 物品稀有度（颜色）
/*new Item.Properties()
    .stacksTo(64) // 创造中键堆叠数量
    .durability(100) // 耐久度
    .rarity(Rarity.RARE) // 物品稀有度（颜色）*/

    // 注册方法：将延迟注册器绑定到事件总线
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);// 在Forge初始化时触发注册
    }
}
