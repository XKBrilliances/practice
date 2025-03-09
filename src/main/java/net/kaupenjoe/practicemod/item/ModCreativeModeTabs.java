package net.kaupenjoe.practicemod.item;

import net.kaupenjoe.practicemod.PracticeMod;
import net.kaupenjoe.practicemod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    // 创建延迟注册器，用于注册创造模式标签页
    public static final DeferredRegister<CreativeModeTab> CRATIVE_MODE_TABS =
            // 参数1: 注册表类型，参数2: 模组ID
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PracticeMod.MODID);
    // 注册自定义创造模式标签页
    public static final RegistryObject<CreativeModeTab> PRACTICE_TAB = CRATIVE_MODE_TABS.register("practice_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SAPPHIRE.get()))// 设置标签页图标（使用蓝宝石物品）
                    .title(Component.translatable("creativetab.practice_tab"))// 设置本地化标题
                    .displayItems((pParameters,pOutput) -> {// 定义标签页内容
                        // 添加模组物品
                        pOutput.accept(ModItems.SAPPHIRE.get());//蓝水晶
                        pOutput.accept(ModItems.RAW_SAPPHIRE.get());//粗蓝水晶矿
                        pOutput.accept(ModItems.METAL_DETECTOR.get());//铁矿检测器
                        pOutput.accept(ModItems.BUFF_LTEM.get());//捏碎给buff

                        // 添加原版物品作为示例
                        pOutput.accept(Items.DIAMOND);
                    })
                    .build());// 构建标签页实例

    public static final RegistryObject<CreativeModeTab> PRACTICE_TAB_2 = CRATIVE_MODE_TABS.register("practice_tab_2",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SAPPHIRE.get()))// 设置标签页图标（使用蓝宝石物品）
                    .title(Component.translatable("creativetab.practice_tab_2"))// 设置本地化标题
                    .displayItems((pParameters,pOutput) -> {// 定义标签页内容
                        // 添加模组物品
                        pOutput.accept(ModBlocks.SAPPHIRE_BLOCK.get());//蓝水晶块
                        pOutput.accept(ModBlocks.RAW_SAPPHIRE_BLOCK.get());//粗蓝水晶矿块
                        pOutput.accept(ModBlocks.SAPPHIRE_ORE.get());//蓝水晶矿石
                        pOutput.accept(ModBlocks.DEEPSLATE_SAPPHIRE_ORE.get());//深层蓝水晶矿石
                        pOutput.accept(ModBlocks.END_STONE_SAPPHIRE_ORE.get());//末地蓝水晶矿石
                        pOutput.accept(ModBlocks.NETHER_SAPPHIRE_ORE.get());//下届蓝水晶矿石
                        pOutput.accept(ModBlocks.SOUND_BLOCK.get());//自定义方块
                    })
                    .build());// 构建标签页实例


    // 注册方法：将延迟注册器绑定到事件总线
    public static void register(IEventBus eventBus) {
        CRATIVE_MODE_TABS.register(eventBus);// 在Forge初始化时触发注册
    }
}
