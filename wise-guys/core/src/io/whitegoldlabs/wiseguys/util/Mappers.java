package io.whitegoldlabs.wiseguys.util;

import com.badlogic.ashley.core.ComponentMapper;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.CollectboxComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.IdleAnimationComponent;
import io.whitegoldlabs.wiseguys.component.InventoryComponent;
import io.whitegoldlabs.wiseguys.component.MovingStateComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;

public class Mappers
{
	public static final ComponentMapper<AccelerationComponent> acceleration = ComponentMapper.getFor(AccelerationComponent.class);
	public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
	public static final ComponentMapper<CollectboxComponent> collectbox = ComponentMapper.getFor(CollectboxComponent.class);
	public static final ComponentMapper<HitboxComponent> hitbox = ComponentMapper.getFor(HitboxComponent.class);
	public static final ComponentMapper<IdleAnimationComponent> idleAnimation = ComponentMapper.getFor(IdleAnimationComponent.class);
	public static final ComponentMapper<InventoryComponent> inventory = ComponentMapper.getFor(InventoryComponent.class);
	public static final ComponentMapper<MovingStateComponent> movingState = ComponentMapper.getFor(MovingStateComponent.class);
	public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
	public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
	public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
}
