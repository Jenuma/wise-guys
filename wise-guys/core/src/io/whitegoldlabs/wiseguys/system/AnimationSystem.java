package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.component.AirborneStateComponent;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.FacingDirectionStateComponent;
import io.whitegoldlabs.wiseguys.component.MovingStateComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class AnimationSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private float animationTime;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public AnimationSystem()
	{
		animationTime = 0;
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.all
		(
			SpriteComponent.class,
			AnimationComponent.class,
			FacingDirectionStateComponent.class,
			MovingStateComponent.class,
			AirborneStateComponent.class
		).get());
	}
	
	@Override
	public void update(float deltaTime)
	{
		for(Entity entity : entities)
		{
			SpriteComponent currentSprite = Mappers.sprite.get(entity);
			AnimationComponent animation = Mappers.animation.get(entity);
			FacingDirectionStateComponent facingDirectionState = Mappers.facingState.get(entity);
			MovingStateComponent movingState = Mappers.movingState.get(entity);
			AirborneStateComponent airborneState = Mappers.airborneState.get(entity);
			
			if(airborneState.currentState == AirborneStateComponent.State.JUMPING)
			{
				currentSprite.sprite = animation.jumpingSprite;
			}
			else
			{
				if(movingState.currentState == MovingStateComponent.State.NOT_MOVING ||
					movingState.currentState == MovingStateComponent.State.SLOWING_LEFT ||
					movingState.currentState == MovingStateComponent.State.SLOWING_RIGHT)
				{
					currentSprite.sprite = animation.standingSprite;
				}
				else
				{
					animationTime += deltaTime;
					currentSprite.sprite = animation.walkingSprites.get(animation.walkingFrame);
					
					if(animationTime > 0.03)
					{
						animationTime = 0;
						animation.walkingFrame++;
						
						if(animation.walkingFrame > 2)
						{
							animation.walkingFrame = 0;
						}
					}
				}
			}
			
			if(facingDirectionState.currentState == FacingDirectionStateComponent.State.FACING_RIGHT)
			{
				currentSprite.sprite.setFlip(false, false);
			}
			else
			{
				currentSprite.sprite.setFlip(true, false);
			}
		}
	}
}
