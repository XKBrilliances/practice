package net.kaupenjoe.practicemod.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class BuffItem extends Item {
    // 每个属性修改器的唯一身份证（防止重复添加）
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID ARMOR_UUID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID HEALTH_BOOST_UUID = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private static final UUID SPEED_BOOST_UUID = UUID.fromString("00000000-0000-0000-0000-000000000004");

    // 记录哪些玩家使用了物品和开始时间（线程安全版记事本）
    private static final Map<Player, Long> attackingPlayersMap = new ConcurrentHashMap<>();
    private static final Map<Player, Long> weaknessPlayersMap = new ConcurrentHashMap<>();

    // 定义各种加成效果（类似游戏内的属性修改器）定义加成数值
    private static final AttributeModifier attackDamageModifier = new AttributeModifier(
            ATTACK_DAMAGE_UUID, "攻击增强", 2.0, AttributeModifier.Operation.ADDITION);  // 直接加2点攻击
    private static final AttributeModifier armorModifier = new AttributeModifier(
            ARMOR_UUID, "护甲增强", 2.0, AttributeModifier.Operation.ADDITION);           // 直接加2点护甲
    private static final AttributeModifier healthBoostModifier = new AttributeModifier(
            HEALTH_BOOST_UUID, "生命增强", 4.0, AttributeModifier.Operation.ADDITION);   // 直接加4点生命
    private static final AttributeModifier speedBoostModifier = new AttributeModifier(
            SPEED_BOOST_UUID, "速度增强", 0.1, AttributeModifier.Operation.MULTIPLY_BASE);// 基础速度乘以1.1倍
    // 物品的构造方法（类似物品的出生证明）
    public BuffItem(Properties pProperties) {
        super(pProperties);
        // 在游戏里注册这个物品需要监听的事件（相当于给物品装了个监听器）
        MinecraftForge.EVENT_BUS.register(this);
    }

    // 当玩家右键使用这个物品时（核心功能都在这里）
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);  // 获取玩家手里的物品

        if (!world.isClientSide) { // 只在服务器端执行（防止重复执行）
            // 给玩家添加5分钟的力量II效果（6000游戏刻 = 5分钟）
            //1tick=0.05
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 1));
            stack.shrink(1);  // 减少1个物品数量

            // 如果物品用完了就播放玻璃碎的声音
            if (stack.isEmpty()) {
                world.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
                player.setItemInHand(hand, ItemStack.EMPTY);  // 清空玩家手上的物品
            }

            // 给玩家添加各种属性加成
            applyAttributeModifiers(player);
            // 记录开始时间（用来计算5分钟倒计时）
            attackingPlayersMap.put(player, System.currentTimeMillis());
        }

        // 在客户端显示酷炫的粒子效果
        if (world.isClientSide) {
            spawnParticleEffect(world, player);
        }

        return InteractionResultHolder.success(stack);
    }

    // 生成环绕玩家的三层魔法粒子圈
    private void spawnParticleEffect(Level level, Player player) {
        if (level.isClientSide) {  // 只在客户端生成粒子
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            // 在头部、身体和腿部各生成一个光环
            generateRing(level, x, y + 1.5, z, 2.0);  // 头部光环
            generateRing(level, x, y + 1.0, z, 2.0);  // 身体光环
            generateRing(level, x, y + 0.5, z, 2.0);  // 腿部光环
        }
    }

    // 生成单个光环的粒子效果（数学计算生成圆形）
    private void generateRing(Level level, double centerX, double centerY, double centerZ, double radius) {
        for (int i = 0; i < 40; i++) {
            double angle = i * 2 * Math.PI / 40;  // 将圆分成40份
            double x = centerX + radius * Math.cos(angle);  // 计算X坐标
            double z = centerZ + radius * Math.sin(angle);  // 计算Z坐标
            // 添加魔法粒子（类似附魔台的效果）
            level.addParticle(ParticleTypes.ENCHANT, x, centerY, z, 0, 0.01, 0);
        }
    }

    // 给玩家添加所有属性加成
    private void applyAttributeModifiers(Player player) {
        // 给玩家各个属性进行添加数值
        player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(attackDamageModifier);
        player.getAttribute(Attributes.ARMOR).addTransientModifier(armorModifier);
        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(healthBoostModifier);
        player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(speedBoostModifier);
        // Minecraft 中的 Attributes 属性列表，用于修改实体（如玩家、怪物等）的各种能力
// 战斗相关属性
// ATTACK_DAMAGE: 实体的攻击力，增加或减少实体造成的伤害
// ATTACK_SPEED: 实体的攻击速度，增加或减少实体攻击的频率
// ARMOR: 实体的护甲值，增加或减少实体的护甲值，影响受到的伤害
// ARMOR_TOUGHNESS: 实体的护甲韧性，增加或减少实体的护甲韧性，影响护甲对高伤害武器的抵抗能力
// KNOCKBACK_RESISTANCE: 实体的抗击退能力，增加或减少实体被击退的距离
// LUCK: 实体的幸运值，增加或减少实体的幸运值，影响掉率等

// 移动相关属性
// MOVEMENT_SPEED: 实体的移动速度，增加或减少实体的移动速度
// FLYING_SPEED: 实体的飞行速度，增加或减少实体的飞行速度
// JUMP_STRENGTH: 实体的跳跃力量，增加或减少实体的跳跃高度
// REACH_DISTANCE: 实体的攻击范围，增加或减少实体攻击的距离
// HAND_ATTACK_REACH: 实体徒手攻击的范围，增加或减少实体徒手攻击的距离

// 生命值相关属性
// MAX_HEALTH: 实体的最大生命值，增加或减少实体的最大生命值

// 行为相关属性
// FOLLOW_RANGE: 实体的追踪范围，增加或减少实体追踪目标的距离
// ZOMBIE_SPAWN_REINFORCEMENTS: 僵尸召唤援军的概率，增加或减少僵尸召唤援军的概率

// 经验值相关属性
// EXPERIENCE: 实体的经验值，增加或减少实体的经验值
// LEVEL: 实体的等级，增加或减少实体的等级
    }

// 移除玩家的所有属性加成
    private static void removeAttributeModifiers(Player player) {
        // 移除攻击力加成
        if (player.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(attackDamageModifier)) {
            player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(ATTACK_DAMAGE_UUID);
        }
        // 移除护甲加成
        if (player.getAttribute(Attributes.ARMOR).hasModifier(armorModifier)) {
            player.getAttribute(Attributes.ARMOR).removeModifier(ARMOR_UUID);
        }
        // 移除生命值加成
        if (player.getAttribute(Attributes.MAX_HEALTH).hasModifier(healthBoostModifier)) {
            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(HEALTH_BOOST_UUID);
        }
        // 移除速度加成
        if (player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(speedBoostModifier)) {
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_BOOST_UUID);
        }
    }
    // 游戏每Tick都会检查的倒计时（类似定时检查器）
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {  // 在每Tick的最后阶段检查
            // 遍历所有记录中的玩家（使用副本避免出错）
            for (Map.Entry<Player, Long> entry : new ConcurrentHashMap<>(attackingPlayersMap).entrySet()) {
                Player player = entry.getKey();
                long startTime = entry.getValue();

                // 如果已经过了5分钟（300000毫秒）
                if (System.currentTimeMillis() - startTime >= 5000) {
                    removeAttributeModifiers(player);  // 移除属性加成
                    attackingPlayersMap.remove(player);  // 从记录中删除
                }
            }

            // 检查并添加虚弱效果
            for (Map.Entry<Player, Long> entry : new ConcurrentHashMap<>(attackingPlayersMap).entrySet()) {
                Player player = entry.getKey();
                long startTime = entry.getValue();

                // 如果已经过了5秒（5000毫秒）
                if (System.currentTimeMillis() - startTime >= 5000) {
                    // 添加虚弱效果，持续10秒（200游戏刻 = 10秒）
                    player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));
                    weaknessPlayersMap.put(player, System.currentTimeMillis());
                }
            }

            // 移除虚弱效果
            for (Map.Entry<Player, Long> entry : new ConcurrentHashMap<>(weaknessPlayersMap).entrySet()) {
                Player player = entry.getKey();
                long startTime = entry.getValue();

                // 如果虚弱效果已经持续了10秒（10000毫秒）
                if (System.currentTimeMillis() - startTime >= 10000) {
                    player.removeEffect(MobEffects.WEAKNESS);
                    weaknessPlayersMap.remove(player);
                }
            }
        }
    }

    // 让物品自带发光效果（类似附魔物品）
    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    // 添加物品的提示文字（玩家悬浮查看时的说明）
    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        // 各种颜色的提示信息
        tooltip.add(Component.literal("使用后会获得藏在水晶中的力量").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.literal("使用后:").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("获得力量Ⅱ").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("攻击力+2").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("护甲值+2").withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal("护甲值只在护甲值不满时才能生效").withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal("生命值+4").withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("速度+10%").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("持续10秒").withStyle(ChatFormatting.DARK_GREEN));
        tooltip.add(Component.literal("物品将会消耗").withStyle(ChatFormatting.DARK_GREEN));
    }
}