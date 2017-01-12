package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.component.HitboxComponent;

public class CollisionSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private ComponentMapper<HitboxComponent> hMap = ComponentMapper.getFor(HitboxComponent.class);
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public CollisionSystem() {}
	
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.all(HitboxComponent.class).get());
	}
	
	public void update(float deltaTime)
	{
		for(int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);
			HitboxComponent hitbox = hMap.get(entity);
			
			// Stuck. :(
		}
	}
}
