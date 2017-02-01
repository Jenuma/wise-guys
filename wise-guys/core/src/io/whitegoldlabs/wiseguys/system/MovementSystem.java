package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class MovementSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private final WiseGuys game;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public MovementSystem(final WiseGuys game)
	{
		this.game = game;
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.all
		(
			PositionComponent.class,
			VelocityComponent.class,
			AccelerationComponent.class,
			StateComponent.class,
			HitboxComponent.class
		).get());
	}
	
	@Override
	public void update(float deltaTime)
	{
		if(game.isRunning)
		{
			for(Entity entity : entities)
			{
				if(!game.eventProcessing || (entity == game.player && game.playerEventMovementAllowed))
				{
					PositionComponent position = Mappers.position.get(entity);
					VelocityComponent velocity = Mappers.velocity.get(entity);
					AccelerationComponent acceleration = Mappers.acceleration.get(entity);
					HitboxComponent hitbox = Mappers.hitbox.get(entity);
					StateComponent state = Mappers.state.get(entity);
					
					// Apply deceleration if slowing.
					if(state.motionState == StateComponent.MotionState.SLOWING &&
						state.directionState == StateComponent.DirectionState.LEFT)
					{
						if(acceleration.x < 0)
						{
							acceleration.x = 0 - acceleration.x;
						}
						
						if(velocity.x + (acceleration.x * deltaTime * 60) >= 0)
						{
							velocity.x = 0;
							acceleration.x = 0;
							state.motionState = StateComponent.MotionState.STILL;
						}
					}
					else if(state.motionState == StateComponent.MotionState.SLOWING &&
							state.directionState == StateComponent.DirectionState.RIGHT)
					{
						if(acceleration.x > 0)
						{
							acceleration.x = 0 - acceleration.x;
						}
						
						if(velocity.x + (acceleration.x * deltaTime * 60) <= 0)
						{
							velocity.x = 0;
							acceleration.x = 0;
							state.motionState = StateComponent.MotionState.STILL;
						}
					}
					
					velocity.x += acceleration.x * deltaTime * 60;
					velocity.y += acceleration.y * deltaTime * 60;
					
					// Keep velocities under max.
					if(velocity.x > 0)
					{
						velocity.x = Math.min(velocity.x, 200);
					}
					else
					{
						velocity.x = Math.max(velocity.x, -200);
					}
					
					if(velocity.y > 0)
					{
						velocity.y = Math.min(velocity.y, 430);
					}
					else
					{
						velocity.y = Math.max(velocity.y, -430);
					}
					
					position.x += velocity.x * deltaTime;
					position.y += velocity.y * deltaTime;
					
					hitbox.hitbox.x = position.x;
					hitbox.hitbox.y = position.y;
				}
			}
		}
	}
}
