package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class AnimationSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private final WiseGuys game;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public AnimationSystem(final WiseGuys game)
	{
		this.game = game;
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		entities = game.engine.getEntitiesFor(Family.all
		(
			AnimationComponent.class
		).get());
	}
	
	@Override
	public void update(float deltaTime)
	{
		if(game.isRunning)
		{
			for(Entity entity : entities)
			{
				AnimationComponent animation = Mappers.animation.get(entity);
				SpriteComponent currentSprite = Mappers.sprite.get(entity);
				StateComponent state = Mappers.state.get(entity);
			
				if(animation.animations.containsKey(state.airborneState.toString()))
				{
					currentSprite.sprite = animation.animations.get(state.airborneState.toString()).getKeyFrame(state.time, true);
				}
				else if(animation.animations.containsKey(state.motionState.toString()))
				{
					currentSprite.sprite = animation.animations.get(state.motionState.toString()).getKeyFrame(state.time, true);
				}
				
				// Flip if facing left.
				if(state.directionState == StateComponent.DirectionState.LEFT)
				{
					currentSprite.sprite.setFlip(true, false);
				}
				else if(state.directionState == StateComponent.DirectionState.RIGHT)
				{
					currentSprite.sprite.setFlip(false, false);
				}
				
				state.time += deltaTime;
			}
		}
	}
}
