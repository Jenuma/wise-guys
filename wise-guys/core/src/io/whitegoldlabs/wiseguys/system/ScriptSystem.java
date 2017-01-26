package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.CollisionComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class ScriptSystem extends EntitySystem
{
	// ---------------------------------------------------------------------------------|
	// Fields                                                                           |
	// ---------------------------------------------------------------------------------|
	private ImmutableArray<Entity> scriptedEntities;
	private ImmutableArray<Entity> hitboxEntities;
	
	private final WiseGuys game;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public ScriptSystem(final WiseGuys game)
	{
		this.game = game;
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		scriptedEntities = engine.getEntitiesFor(Family.all
		(
			ScriptComponent.class
		).get());
		
		hitboxEntities = engine.getEntitiesFor(Family.all
		(
			HitboxComponent.class
		).get());
	}
	
	// ---------------------------------------------------------------------------------|
	// Update                                                                           |
	// ---------------------------------------------------------------------------------|
	@Override
	public void update(float deltaTime)
	{
		if(game.isRunning)
		{
			for(int i = 0; i < scriptedEntities.size(); i++)
			{
				Entity scriptedEntity = scriptedEntities.get(i);
				
				for(int j = 0; j < hitboxEntities.size(); j++)
				{
					Entity hitboxEntity = hitboxEntities.get(j);
					
					if(scriptedEntity != hitboxEntity)
					{
						if(Mappers.hitbox.get(scriptedEntity).hitbox.overlaps(Mappers.hitbox.get(hitboxEntity).hitbox))
						{
							scriptedEntity.add(new CollisionComponent(hitboxEntity));
							game.scriptManager.queueScriptToExecute(Mappers.script.get(scriptedEntity));
						}
					}
				}
			}
		}
	}
}
