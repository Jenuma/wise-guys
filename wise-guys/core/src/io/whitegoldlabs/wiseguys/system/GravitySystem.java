package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.AirborneStateComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

import static io.whitegoldlabs.wiseguys.component.AirborneStateComponent.State;

public class GravitySystem extends EntitySystem
{
	private ImmutableArray<Entity> dynamicEntities;
	private ImmutableArray<Entity> obstacleEntities;
	
	private final float G = -25;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public GravitySystem() {}
	
	public void addedToEngine(Engine engine)
	{
		dynamicEntities = engine.getEntitiesFor(Family.all
		(
				AirborneStateComponent.class,
				VelocityComponent.class,
				HitboxComponent.class
		).get());
		
		obstacleEntities = engine.getEntitiesFor(Family.all
		(
			HitboxComponent.class
		)
		.exclude
		(
			VelocityComponent.class,
			AccelerationComponent.class
		).get());
	}
	
	public void update(float deltaTime)
	{
		for(Entity dynamicEntity : dynamicEntities)
		{
			VelocityComponent velocity = Mappers.velocity.get(dynamicEntity);
			AccelerationComponent acceleration = Mappers.acceleration.get(dynamicEntity);
			State airbornState = Mappers.airborneState.get(dynamicEntity).currentState;
			
			// If the entity is falling, apply gravity.
			if(airbornState == State.FALLING || airbornState == State.JUMPING)
			{
				//velocity.y += G * deltaTime * 60;
				acceleration.y = G * deltaTime * 60;
			}
			// If the entity isn't falling, check to see if it should be.
			else
			{
				velocity.y = 0;
				acceleration.y = 0;
				
				Rectangle fallbox = new Rectangle(Mappers.hitbox.get(dynamicEntity).hitbox);
				fallbox.y--;
				
				for(Entity obstacle : obstacleEntities)
				{
					Rectangle obstacleHitbox = Mappers.hitbox.get(obstacle).hitbox;
					if(fallbox.overlaps(obstacleHitbox))
					{
						return;
					}
				}
				
				Mappers.airborneState.get(dynamicEntity).currentState = AirborneStateComponent.State.FALLING;
			}
		}
	}
}
