package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class MovementSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public MovementSystem() {}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.all
		(
			PositionComponent.class,
			VelocityComponent.class,
			HitboxComponent.class
		).get());
	}
	
	@Override
	public void update(float deltaTime)
	{
		for(Entity entity : entities)
		{
			PositionComponent position = Mappers.position.get(entity);
			VelocityComponent velocity = Mappers.velocity.get(entity);
			HitboxComponent hitbox = Mappers.hitbox.get(entity);
			
			position.x += velocity.x * deltaTime;
			hitbox.hitbox.x = position.x;
			
			position.y += velocity.y * deltaTime;
			hitbox.hitbox.y = position.y;
		}
	}
}
