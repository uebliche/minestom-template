package net.uebliche.server.entity;

import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.ai.goal.DoNothingGoal;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.utils.time.TimeUnit;

public class ZombieCreature extends EntityCreature {
    
    public ZombieCreature() {
        super(EntityType.ZOMBIE);
        addAIGroup(
        new EntityAIGroupBuilder()
        .addGoalSelector(new DoNothingGoal(this, 500, .1F))
        .addGoalSelector(new MeleeAttackGoal(this, 500, 2, TimeUnit.MILLISECOND))
        .addGoalSelector(new RandomStrollGoal(this, 2))
        .addTargetSelector(new LastEntityDamagerTarget(this, 15))
        .addTargetSelector(new ClosestEntityTarget(this, 15, LivingEntity.class))
        .build());
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.1);
        
    }
    
}
