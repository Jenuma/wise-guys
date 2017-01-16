package io.whitegoldlabs.wiseguys.util;

import com.badlogic.ashley.core.ComponentMapper;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.AirbornStateComponent;
import io.whitegoldlabs.wiseguys.component.CollectboxComponent;
import io.whitegoldlabs.wiseguys.component.FacingDirectionStateComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.InventoryComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;

public class Mappers
{
	public static final ComponentMapper<AccelerationComponent> acceleration = ComponentMapper.getFor(AccelerationComponent.class);
	public static final ComponentMapper<CollectboxComponent> collectbox = ComponentMapper.getFor(CollectboxComponent.class);
	public static final ComponentMapper<FacingDirectionStateComponent> facingState = ComponentMapper.getFor(FacingDirectionStateComponent.class);
	public static final ComponentMapper<HitboxComponent> hitbox = ComponentMapper.getFor(HitboxComponent.class);
	public static final ComponentMapper<InventoryComponent> inventory = ComponentMapper.getFor(InventoryComponent.class);
	public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
	public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
	public static final ComponentMapper<AirbornStateComponent> airbornState = ComponentMapper.getFor(AirbornStateComponent.class);
	public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
}
