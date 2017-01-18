package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class AnimationSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public AnimationSystem() {}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.all
		(
			AnimationComponent.class
		).get());
	}
	
	@Override
	public void update(float deltaTime)
	{
		for(Entity entity : entities)
		{
			AnimationComponent animation = Mappers.animation2.get(entity);
			SpriteComponent currentSprite = Mappers.sprite.get(entity);
			StateComponent state = Mappers.movingState.get(entity);
		
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
			else
			{
				currentSprite.sprite.setFlip(false, false);
			}
			
			state.time += deltaTime;
		}
	}
}
