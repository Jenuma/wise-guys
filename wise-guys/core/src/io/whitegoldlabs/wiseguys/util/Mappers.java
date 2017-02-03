package io.whitegoldlabs.wiseguys.util;

import com.badlogic.ashley.core.ComponentMapper;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.BehaviorComponent;
import io.whitegoldlabs.wiseguys.component.CollisionComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PhaseComponent;
import io.whitegoldlabs.wiseguys.component.PlayerComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.TagComponent;
import io.whitegoldlabs.wiseguys.component.TypeComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;

public class Mappers
{
	public static final ComponentMapper<AccelerationComponent> acceleration = ComponentMapper.getFor(AccelerationComponent.class);
	public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
	public static final ComponentMapper<BehaviorComponent> behavior = ComponentMapper.getFor(BehaviorComponent.class);
	public static final ComponentMapper<CollisionComponent> collision = ComponentMapper.getFor(CollisionComponent.class);
	public static final ComponentMapper<HitboxComponent> hitbox = ComponentMapper.getFor(HitboxComponent.class);
	public static final ComponentMapper<PhaseComponent> phase = ComponentMapper.getFor(PhaseComponent.class);
	public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
	public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
	public static final ComponentMapper<ScriptComponent> script = ComponentMapper.getFor(ScriptComponent.class);
	public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
	public static final ComponentMapper<StateComponent> state = ComponentMapper.getFor(StateComponent.class);
	public static final ComponentMapper<TagComponent> tag = ComponentMapper.getFor(TagComponent.class);
	public static final ComponentMapper<TypeComponent> type = ComponentMapper.getFor(TypeComponent.class);
	public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
}
