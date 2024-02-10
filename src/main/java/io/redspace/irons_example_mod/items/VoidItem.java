package io.redspace.irons_example_mod.items;


import com.google.common.collect.Multimap;
import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class VoidItem extends Item{
    public VoidItem(Item.Properties pProperties){
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.voiditem"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {

        Attribute attr = Attributes.ATTACK_DAMAGE;
        AttributeModifier mod = new AttributeModifier("Damage", 4.0, AttributeModifier.Operation.ADDITION);
        stack.addAttributeModifier(attr,mod,EquipmentSlot.MAINHAND);

        Attribute attr2 = Attributes.ATTACK_SPEED;
        AttributeModifier mod2 = new AttributeModifier("Speed", 4.0, AttributeModifier.Operation.ADDITION);
        stack.addAttributeModifier(attr2,mod2,EquipmentSlot.MAINHAND);

        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {

        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        level.destroyBlock(pos,true);


        return super.useOn(pContext);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {

        player.sendSystemMessage(Component.literal("found"));
        player.getCommandSenderWorld()
                .destroyBlock(pos,true);

        return super.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {


        player.setHealth(20);
        entity.kill();

        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {

        pPlayer.setHealth(20);
        pInteractionTarget.kill();

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {

        Vec3 pos = player.getEyePosition();
        Vec3 v = player.getLookAngle();
        for (int i = 0;i < 50 ; i++) {


            pos = pos.add(v.x * 0.5,v.y * 0.5,v.z * 0.5);
            level.addParticle(ParticleTypes.DRAGON_BREATH,pos.x,pos.y,pos.z,0,0,0);
            level.addParticle(ParticleTypes.ENCHANT,pos.x,pos.y,pos.z,0,0,0);
            List<LivingEntity> ent = level.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(pos, 0.1, 0.1, 0.1));
            for (LivingEntity l : ent) {

                if (l != player) {
                    l.kill();
                    for(int j = 0; j < 50; j++) {
                        level.addParticle(ParticleTypes.SCULK_CHARGE_POP, pos.x + (Math.random()), pos.y + (Math.random()), pos.z + (Math.random()), 0,0,0);
                    }
                    i = 50;
                }
            }
        }


      /*  for(Entity ent : list){

            if(player.hasLineOfSight(ent)){
            ent.kill();
            }

        }*/


        return super.use(level, player, pUsedHand);
    }
}
