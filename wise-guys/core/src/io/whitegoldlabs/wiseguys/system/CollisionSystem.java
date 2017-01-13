package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class CollisionSystem extends EntitySystem
{
	private ImmutableArray<Entity> dynamicEntities;
	private ImmutableArray<Entity> obstacleEntities;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public CollisionSystem() {}
	
	public void addedToEngine(Engine engine)
	{
		dynamicEntities = engine.getEntitiesFor(Family.all
		(
			StateComponent.class,
			HitboxComponent.class,
			PositionComponent.class,
			VelocityComponent.class,
			AccelerationComponent.class
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
			PositionComponent dynamicEntityPosition = Mappers.position.get(dynamicEntity);
			VelocityComponent dynamicEntityVelocity = Mappers.velocity.get(dynamicEntity);
			AccelerationComponent dynamicEntityAcceleration = Mappers.acceleration.get(dynamicEntity);
			HitboxComponent dynamicEntityHitbox = Mappers.hitbox.get(dynamicEntity);
			
			for(Entity obstacleEntity : obstacleEntities)
			{
				HitboxComponent obstacleHitbox = Mappers.hitbox.get(obstacleEntity);
				
				if(dynamicEntityHitbox.hitbox.overlaps(obstacleHitbox.hitbox))
				{
					Rectangle intersection = getIntersection(dynamicEntityHitbox.hitbox, obstacleHitbox.hitbox);
					
					boolean hitFromAbove = intersection.y > obstacleHitbox.hitbox.y;
					boolean hitFromBelow = intersection.y > dynamicEntityHitbox.hitbox.y;
					boolean hitFromLeft = intersection.x > dynamicEntityHitbox.hitbox.x;
					boolean hitFromRight = intersection.x > obstacleHitbox.hitbox.x;
					
					if(hitFromBelow)
					{
						dynamicEntityPosition.y = obstacleHitbox.hitbox.y - dynamicEntityHitbox.hitbox.height;
						
						dynamicEntityVelocity.y = 0;
						dynamicEntityAcceleration.y = 0;
					}
					else if(hitFromAbove)
					{
						Mappers.state.get(dynamicEntity).currentState = StateComponent.State.ON_GROUND;
						
						dynamicEntityPosition.y = obstacleHitbox.hitbox.y + obstacleHitbox.hitbox.height;
						
						dynamicEntityVelocity.y = 0;
						dynamicEntityAcceleration.y = 0;
					}
					else if(hitFromRight)
					{
						dynamicEntityPosition.x = obstacleHitbox.hitbox.x + obstacleHitbox.hitbox.width;
						
						dynamicEntityVelocity.x = 0;
						dynamicEntityAcceleration.x = 0;
						
						System.out.println("HIT FROM RIGHT: BLOCK @ " + obstacleHitbox.hitbox.x + ", " + obstacleHitbox.hitbox.y + " PLAYER PUSHED RIGHT");
					}
					else if(hitFromLeft)
					{
						dynamicEntityPosition.x = obstacleHitbox.hitbox.x - obstacleHitbox.hitbox.width;
						
						dynamicEntityVelocity.x = 0;
						dynamicEntityAcceleration.x = 0;
						
						System.out.println("HIT FROM LEFT: BLOCK @ " + obstacleHitbox.hitbox.x + ", " + obstacleHitbox.hitbox.y + " PLAYER PUSHED LEFT");
					}
				}
			}
		}
	}
	
	private Rectangle getIntersection(Rectangle rect1, Rectangle rect2)
	{
		Rectangle intersection = new Rectangle();
		
		intersection.x = Math.max(rect1.x, rect2.x);
		intersection.y = Math.max(rect1.y, rect2.y);
		intersection.width = Math.min(rect1.x + rect1.width, rect2.x + rect2.width) - intersection.x;
		intersection.height = Math.min(rect1.y + rect1.height, rect2.y + rect2.height) - intersection.y;
		
		return intersection;

	}
}
