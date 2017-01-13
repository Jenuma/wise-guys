package io.whitegoldlabs.wiseguys.util;

import com.badlogic.ashley.core.ComponentMapper;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;

public class Mappers
{
	public static final ComponentMapper<AccelerationComponent> acceleration = ComponentMapper.getFor(AccelerationComponent.class);
	public static final ComponentMapper<HitboxComponent> hitbox = ComponentMapper.getFor(HitboxComponent.class);
	public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
	public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
	public static final ComponentMapper<StateComponent> state = ComponentMapper.getFor(StateComponent.class);
	public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
}
