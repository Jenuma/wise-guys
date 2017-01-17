package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.IdleAnimationComponent;
import io.whitegoldlabs.wiseguys.component.MovingStateComponent;
import io.whitegoldlabs.wiseguys.component.MovingStateComponent.AirborneState;
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
			MovingStateComponent.class
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
			MovingStateComponent movingState = Mappers.movingState.get(entity);
			
			if(movingState.airborneState == AirborneState.JUMPING)
			{
				if(animation.jumpingSprites != null)
				{
					if(animation.jumpingSprites.size == 0)
					{
						currentSprite.sprite = animation.jumpingSprites.get(0);
					}
					else
					{
						animation.jumpingAnimationTime += deltaTime;
						currentSprite.sprite = animation.jumpingSprites.get(animation.jumpingFrame);
						
						if(animation.jumpingAnimationTime > 0.01)
						{
							animation.jumpingAnimationTime = 0;
							animation.jumpingFrame++;
							
							if(animation.jumpingFrame >= animation.jumpingSprites.size)
							{
								animation.jumpingFrame = 0;
							}
						}
					}
				}
			}
			else
			{
				switch(movingState.motionState)
				{
					case STILL:
						if(animation.stillSprites != null)
						{
							if(animation.stillSprites.size == 0)
							{
								currentSprite.sprite = animation.stillSprites.get(0);
							}
							else
							{
								animation.stillAnimationTime += deltaTime;
								currentSprite.sprite = animation.stillSprites.get(animation.stillFrame);
								
								if(animation.stillAnimationTime > 0.01)
								{
									animation.stillAnimationTime = 0;
									animation.stillFrame++;
									
									if(animation.stillFrame >= animation.stillSprites.size)
									{
										animation.stillFrame = 0;
									}
								}
							}
						}
						
						break;
					case MOVING:
						if(animation.movingSprites != null)
						{
							if(animation.movingSprites.size == 0)
							{
								currentSprite.sprite = animation.movingSprites.get(0);
							}
							else
							{
								animation.movingAnimationTime += deltaTime;
								currentSprite.sprite = animation.movingSprites.get(animation.movingFrame);
								
								if(animation.movingAnimationTime > 0.03)
								{
									animation.movingAnimationTime = 0;
									animation.movingFrame++;
									
									if(animation.movingFrame >= animation.movingSprites.size)
									{
										animation.movingFrame = 0;
									}
								}
							}
						}
						
						break;
					case SLOWING:
						if(animation.slowingSprites != null)
						{
							if(animation.slowingSprites.size == 0)
							{
								currentSprite.sprite = animation.slowingSprites.get(0);
							}
							else
							{
								animation.slowingAnimationTime += deltaTime;
								currentSprite.sprite = animation.slowingSprites.get(animation.slowingFrame);
								
								if(animation.slowingAnimationTime > 0.01)
								{
									animation.slowingAnimationTime = 0;
									animation.slowingFrame++;
									
									if(animation.slowingFrame >= animation.slowingSprites.size)
									{
										animation.slowingFrame = 0;
									}
								}
							}
						}
						
						break;
					case SPRINTING:
						if(animation.sprintingSprites != null)
						{
							if(animation.sprintingSprites.size == 0)
							{
								currentSprite.sprite = animation.sprintingSprites.get(0);
							}
							else
							{
								animation.sprintingAnimationTime += deltaTime;
								currentSprite.sprite = animation.sprintingSprites.get(animation.sprintingFrame);
								
								if(animation.sprintingAnimationTime > 0.01)
								{
									animation.sprintingAnimationTime = 0;
									animation.sprintingFrame++;
									
									if(animation.sprintingFrame >= animation.sprintingSprites.size)
									{
										animation.sprintingFrame = 0;
									}
								}
							}
						}
						
						break;
				}
			}
			
			// Flip the sprite if not facing right.
			if(movingState.directionState == MovingStateComponent.DirectionState.RIGHT)
			{
				currentSprite.sprite.setFlip(false, false);
			}
			else
			{
				currentSprite.sprite.setFlip(true, false);
			}
		}
		
		// Idle Animations
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
