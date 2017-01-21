package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;
import io.whitegoldlabs.wiseguys.util.ScriptManager;

public class ScriptSystem extends EntitySystem
{
	// ---------------------------------------------------------------------------------|
	// Fields                                                                           |
	// ---------------------------------------------------------------------------------|
	private ImmutableArray<Entity> scriptedEntities;
	
	private Entity player;
	private ScriptManager scriptManager;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public ScriptSystem(Entity player, ScriptManager scriptManager)
	{
		this.player = player;
		this.scriptManager = scriptManager;
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
		// Handle collisions between the player and scripted entities.
		for(Entity scriptedEntity : scriptedEntities)
		{
			if(Mappers.hitbox.get(player).hitbox.overlaps(Mappers.hitbox.get(scriptedEntity).hitbox))
			{
				scriptManager.setScriptToExecute(Mappers.script.get(scriptedEntity));
				break;
			}
		}
	}
}
