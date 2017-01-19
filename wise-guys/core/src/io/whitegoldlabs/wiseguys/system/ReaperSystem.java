package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

import static io.whitegoldlabs.wiseguys.component.StateComponent.EnabledState;

public class ReaperSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	private Engine engine;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public ReaperSystem(Engine engine)
	{
		this.engine = engine;
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
		for(Entity entity : entities)
		{
			if(Mappers.state.get(entity).enabledState == EnabledState.DISABLED)
			{
				engine.removeEntity(entity);
			}
		}
	}
}
