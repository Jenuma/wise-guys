package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.CollisionComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class ScriptSystem extends EntitySystem
{
	// ---------------------------------------------------------------------------------|
	// Fields                                                                           |
	// ---------------------------------------------------------------------------------|
	private ImmutableArray<Entity> dynamicEntities;
	private ImmutableArray<Entity> scriptedEntities;
	
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
		dynamicEntities = engine.getEntitiesFor(Family.all
		(
			HitboxComponent.class,
			VelocityComponent.class,
			AccelerationComponent.class
		).get());
		
		scriptedEntities = engine.getEntitiesFor(Family.all
		(
			ScriptComponent.class
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
			for(int i = 0; i < dynamicEntities.size(); i++)
			{
				Entity dynamicEntity = dynamicEntities.get(i);
				
				for(int j = 0; j < scriptedEntities.size(); j++)
				{
					Entity scriptedEntity = scriptedEntities.get(j);
					
					if(dynamicEntity != scriptedEntity)
					{
						if(Mappers.hitbox.get(dynamicEntity).hitbox.overlaps(Mappers.hitbox.get(scriptedEntity).hitbox))
						{
							scriptedEntity.add(new CollisionComponent(dynamicEntity));
							game.scriptManager.queueScriptToExecute(Mappers.script.get(scriptedEntity));
						}
					}
				}
			}
		}
	}
}
