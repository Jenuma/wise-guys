package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class ScriptSystem extends EntitySystem
{
	// ---------------------------------------------------------------------------------|
	// Fields                                                                           |
	// ---------------------------------------------------------------------------------|
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
			// Handle collisions between the player and scripted entities.
			for(Entity scriptedEntity : scriptedEntities)
			{
				if(Mappers.hitbox.get(game.player).hitbox.overlaps(Mappers.hitbox.get(scriptedEntity).hitbox))
				{
					game.scriptManager.setScriptToExecute(Mappers.script.get(scriptedEntity));
					break;
				}
			}
		}
	}
}
