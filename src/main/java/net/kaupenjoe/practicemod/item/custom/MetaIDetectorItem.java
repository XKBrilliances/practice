package net.kaupenjoe.practicemod.item.custom;

import net.kaupenjoe.practicemod.block.ModBlocks;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

// 自定义物品类：元数据检测器（用于检测特定方块的坐标）
public class MetaIDetectorItem extends Item {
    // 构造函数，接收物品属性配置
    public MetaIDetectorItem(Properties pProperties) {
        super(pProperties); // 调用父类Item的构造函数
    }

    @Override
    // 当玩家右键点击方块时触发此方法
    public InteractionResult useOn(UseOnContext pContext) {
        // 仅在服务端执行逻辑（避免客户端重复处理）
        //pContext名称
        /*.getLevel()
           获取当前所处的游戏世界（Level 对象），相当于获取当前维度/世界的实例
         .isClientSide()
           关键判断方法，返回 boolean 值：
             true：当前在客户端（玩家视角所在的世界）
             false：当前在服务端（游戏逻辑实际处理端）*/
        if (!pContext.getLevel().isClientSide()) {
            BlockPos positionClicked = pContext.getClickedPos(); // 获取点击的方块坐标
            Player player = pContext.getPlayer(); // 获取使用物品的玩家
            boolean foundBlock = false; // 标记是否找到目标方块

            // 遍历从点击位置向下最多64层的方块（存在潜在逻辑错误，见注释）
            for (int i = 0; i <= positionClicked.getX() + 64; i++) {
                BlockState state = pContext.getLevel().getBlockState(positionClicked.below(i)); // 获取下方第i层的方块状态

                // 检查当前方块是否为需要检测的目标（如铁矿或钻石矿）
                if (isValuableBlock(state)) {
                    // 输出方块坐标和名称给玩家
                    outputValuableCoordinates(positionClicked.below(i), player, state.getBlock());
                    foundBlock = true;
                    break; // 找到后终止循环
                }
            }

            // 未找到目标方块时提示玩家
            if (!foundBlock) {
                player.sendSystemMessage(Component.literal("未找到方块"));
            }
        }

        // 消耗物品耐久（每次使用减少1点）
        pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(),
                player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return InteractionResult.SUCCESS; // 返回交互成功状态
    }

    // 向玩家发送系统消息，包含方块名称和坐标
    private void outputValuableCoordinates(BlockPos blockPos, Player player, Block block) {
        // 使用I18n获取本地化方块名称（需注意客户端依赖）
        player.sendSystemMessage(Component.literal("找到 " + I18n.get(block.getDescriptionId()) + " 在 (" +
                blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ() + ")"));
    }

    // 判断方块是否为需要检测的目标（铁矿或钻石矿）
    private boolean isValuableBlock(BlockState state) {
        return state.is(Blocks.IRON_ORE) ||
                state.is(Blocks.DIAMOND_ORE) ||
                state.is(Blocks.GOLD_ORE) ||
                state.is(Blocks.EMERALD_ORE) ||
                state.is(Blocks.NETHER_QUARTZ_ORE) ||
                state.is(ModBlocks.SAPPHIRE_ORE.get());//检测mod方块一定要带上RegistryObject<Block>中的.get()方法获取
    }
}