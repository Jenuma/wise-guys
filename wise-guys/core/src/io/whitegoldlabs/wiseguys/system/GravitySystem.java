package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.component.VelocityComponent;

public class GravitySystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private ComponentMapper<VelocityComponent> vMap = ComponentMapper.getFor(VelocityComponent.class);
	
	private final float G = -25;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public GravitySystem() {}
	
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.all(VelocityComponent.class).get());
	}
	
	public void update(float deltaTime)
	{
		for(int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);
			VelocityComponent velocity = vMap.get(entity);
			
			velocity.y += G * deltaTime * 60;
		}
	}
}
