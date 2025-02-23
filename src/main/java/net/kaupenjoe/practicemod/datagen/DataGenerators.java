package net.kaupenjoe.practicemod.datagen;

import net.kaupenjoe.practicemod.PracticeMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

/**
 * 数据生成器主类，用于注册各类数据生成器
 * 监听Forge的GatherDataEvent事件，在数据生成阶段创建所需数据
 */
@Mod.EventBusSubscriber(modid = PracticeMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    /**
     * 数据生成事件处理方法（方法名可能存在拼写错误，应为gatherData）
     * @param event 数据收集事件，包含生成所需的各种上下文信息
     */
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // 获取核心工具类实例
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput(); // 数据包输出路径
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper(); // 文件存在校验帮助类
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider(); // 注册项查询接口

        // 注册服务端数据生成器（仅服务端运行）
        // 添加合成配方生成器
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
        // 添加战利品表生成器
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(packOutput));

        /*
         * 以下为被注释的客户端数据生成器（需同时包含客户端和服务端时使用）
         */
        // 添加方块状态生成器（客户端）
        // generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        // 添加物品模型生成器（客户端）
        // generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));

        /*
         * 方块标签生成器（服务端）
         * 需要分两个阶段处理标签依赖关系
         */
        // 首先生成基础方块标签
        // ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
        //         new ModBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
        // 然后生成依赖其他标签的扩展标签
        // generator.addProvider(event.includeServer(), new ModItemTagGenerator(
        //         packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
    }
}