package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.BehaviorComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class BehaviorSystem extends EntitySystem
{
	private final WiseGuys game;
	
	private ImmutableArray<Entity> entities;
	
	public BehaviorSystem(final WiseGuys game)
	{
		this.game = game;
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family
				.all(BehaviorComponent.class)
		.get());
	}
	
	@Override
	public void update(float deltaTime)
	{
		for(Entity entity : entities)
		{
			BehaviorComponent behavior = Mappers.behavior.get(entity);
			
			behavior.behaviorTime += deltaTime;
			game.scriptManager.executeScript(behavior.moduleName, behavior.args);
		}
	}
}
