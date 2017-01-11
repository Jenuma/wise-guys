package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;

public class MovementSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	ComponentMapper<PositionComponent> pMap = ComponentMapper.getFor(PositionComponent.class);
	ComponentMapper<VelocityComponent> vMap = ComponentMapper.getFor(VelocityComponent.class);
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public MovementSystem() {}
	
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
	}
	
	public void update(float deltaTime)
	{
		for(int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);
			PositionComponent position = pMap.get(entity);
			VelocityComponent velocity = vMap.get(entity);
			
			position.x += velocity.x * deltaTime;
			position.y += velocity.y * deltaTime;
		}
	}
}
