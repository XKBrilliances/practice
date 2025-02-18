package net.kaupenjoe.practicemod.datagen;

import net.kaupenjoe.practicemod.datagen.loot.ModBlockLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

/**
 * 模组战利品表数据生成入口类
 * 负责协调不同战利品表子生成器的注册
 */
public class ModLootTableProvider {

    /**
     * 创建战利品表数据生成器
     * @param output 数据输出路径（由Minecraft自动注入）
     * @return 配置完成的战利品表生成器实例
     */
    public static LootTableProvider create(PackOutput output){
        return new LootTableProvider(
                output,// 数据输出目录
                Set.of(),// 需要排除的命名空间（空集合表示不排除任何模组）
                List.of(// 注册所有战利品表子生成器
                        // 方块战利品表生成器（处理方块掉落
                new LootTableProvider.SubProviderEntry(
                        ModBlockLootTables::new,// 使用ModBlockLootTables生成方块掉落
                        LootContextParamSets.BLOCK)// 指定处理方块类型的战利品表
        ));
    }
}
