package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.component.AirborneStateComponent;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.FacingDirectionStateComponent;
import io.whitegoldlabs.wiseguys.component.IdleAnimationComponent;
import io.whitegoldlabs.wiseguys.component.MovingStateComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class AnimationSystem extends EntitySystem
{
	private ImmutableArray<Entity> movingEntities;
	private ImmutableArray<Entity> idleEntities;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public AnimationSystem() {}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		movingEntities = engine.getEntitiesFor(Family.all
		(
			SpriteComponent.class,
			AnimationComponent.class,
			FacingDirectionStateComponent.class,
			MovingStateComponent.class,
			AirborneStateComponent.class
		).get());
		
		idleEntities = engine.getEntitiesFor(Family.all
		(
			SpriteComponent.class,
			IdleAnimationComponent.class
		).get());
	}
	
	@Override
	public void update(float deltaTime)
	{
		for(Entity entity : movingEntities)
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
					animation.movingAnimationTime += deltaTime;
					currentSprite.sprite = animation.walkingSprites.get(animation.walkingFrame);
					
					if(animation.movingAnimationTime > 0.03)
					{
						animation.movingAnimationTime = 0;
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
		
		for(Entity entity : idleEntities)
		{
			IdleAnimationComponent idleAnimation = Mappers.idleAnimation.get(entity);
			SpriteComponent currentSprite = Mappers.sprite.get(entity);
			float interval = 0;
			
			idleAnimation.idleAnimationTime += deltaTime;
			currentSprite.sprite = idleAnimation.idleSprites.get(idleAnimation.idleFrame);
			
			if(idleAnimation.idleFrame == 0)
			{
				interval = 0.5f;
			}
			else
			{
				interval = 0.2f;
			}
			
			if(idleAnimation.idleAnimationTime > interval)
			{
				idleAnimation.idleAnimationTime = 0;
				idleAnimation.idleFrame++;
				
				if(idleAnimation.idleFrame > 3)
				{
					idleAnimation.idleFrame = 0;
				}
			}
		}
	}
}
