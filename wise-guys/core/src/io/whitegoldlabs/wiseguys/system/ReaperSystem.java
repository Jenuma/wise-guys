package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent.EnabledState;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class ReaperSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private final WiseGuys game;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public ReaperSystem(final WiseGuys game)
	{
		this.game = game;
	}
	
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.all
		(
			StateComponent.class
		).get());
	}
	
	public void update(float deltaTime)
	{
		if(game.isRunning)
		{
			for(Entity entity : entities)
			{
				if(Mappers.state.get(entity).enabledState == EnabledState.DISABLED)
				{
					game.engine.removeEntity(entity);
				}
			}
		}
	}
}
