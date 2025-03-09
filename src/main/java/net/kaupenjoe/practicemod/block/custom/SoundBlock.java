// 声明这个类所在的包路径（类似文件夹路径）
package net.kaupenjoe.practicemod.block.custom;

// 导入需要的Minecraft类和资源
import net.minecraft.core.BlockPos; // 方块的坐标位置
import net.minecraft.sounds.SoundEvents; // 游戏中的声音事件
import net.minecraft.sounds.SoundSource; // 声音的来源分类
import net.minecraft.world.InteractionHand; // 玩家交互使用的手（主手/副手）
import net.minecraft.world.InteractionResult; // 交互结果类型
import net.minecraft.world.entity.player.Player; // 玩家实体
import net.minecraft.world.level.Level; // 游戏世界
import net.minecraft.world.level.block.Block; // 基础方块类
import net.minecraft.world.level.block.state.BlockState; // 方块状态
import net.minecraft.world.phys.BlockHitResult; // 方块被点击的位置信息

// 创建自定义声音方块类，继承自基础方块类
public class SoundBlock extends Block {
    // 构造函数：当创建SoundBlock实例时调用
    public SoundBlock(Properties pProperties) {
        super(pProperties); // 调用父类Block的构造函数，传入方块属性
    }

    // 重写（覆盖）use方法：当玩家右键点击方块时触发
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        // 在游戏世界中播放声音
        pLevel.playSound(
                pPlayer,        // 触发声音的玩家（可以为null）
                pPos,           // 方块的坐标位置（声音发出的位置）
                SoundEvents.NOTE_BLOCK_DIDGERIDOO.get(), // 使用迪吉里杜管的声音
                SoundSource.BLOCKS, // 声音来源分类为方块（影响音量衰减和显示方式）
                1.0F,          // 音量（1.0是最大音量，0.5是半音量）
                1.0F           // 音调（1.0是原速，2.0是高八度，0.5是低八度）
        );

        // 返回交互成功的结果（会让游戏播放"使用成功"的动画）
        return InteractionResult.SUCCESS;
    }
}