package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.AirbornStateComponent;
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
			AirbornStateComponent.class,
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
		for(Entity entity : dynamicEntities)
		{
			while(isColliding(entity))
			{	
				float xDistanceToResolve = xDistanceToMoveToResolveCollisions(entity);
				float yDistanceToResolve = yDistanceToMoveToResolveCollisions(entity);
				Vector2 xAndYDistanceToResolve = xAndYDistanceToMoveToResolveCollisions(entity);
				float xAndYTotalDistanceToResolve = Math.abs(xAndYDistanceToResolve.x) + Math.abs(xAndYDistanceToResolve.y);
				
				if(Math.abs(xDistanceToResolve) < Math.abs(yDistanceToResolve) && Math.abs(xDistanceToResolve) < xAndYTotalDistanceToResolve)
				{
					Mappers.position.get(entity).x += xDistanceToResolve;
					Mappers.hitbox.get(entity).hitbox.x += xDistanceToResolve;
					Mappers.velocity.get(entity).x = 0;
				}
				else if(Math.abs(yDistanceToResolve) < Math.abs(xDistanceToResolve) && Math.abs(yDistanceToResolve) < xAndYTotalDistanceToResolve)
				{
					if(yDistanceToResolve > 0)
					{
						Mappers.airbornState.get(entity).currentState = AirbornStateComponent.State.ON_GROUND;
					}
					
					Mappers.position.get(entity).y += yDistanceToResolve;
					Mappers.hitbox.get(entity).hitbox.y += yDistanceToResolve;
					Mappers.velocity.get(entity).y = 0;
				}
				else
				{
					if(xAndYDistanceToResolve.y > 0)
					{
						Mappers.airbornState.get(entity).currentState = AirbornStateComponent.State.ON_GROUND;
					}
					
					Mappers.position.get(entity).x += xAndYDistanceToResolve.x;
					Mappers.position.get(entity).y += xAndYDistanceToResolve.y;
					Mappers.hitbox.get(entity).hitbox.x += xAndYDistanceToResolve.x;
					Mappers.hitbox.get(entity).hitbox.y += xAndYDistanceToResolve.y;
					Mappers.velocity.get(entity).x = 0;
					Mappers.velocity.get(entity).y = 0;
				}
			}
		}
	}
	
	private boolean isColliding(Entity entity)
	{
		for(Entity obstacle : obstacleEntities)
		{
			if(Mappers.hitbox.get(entity).hitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private float xDistanceToMoveToResolveCollisions(Entity entity)
	{
		float rightDistance = findDistanceToEmptySpaceAlongX(entity);
		float leftDistance = findDistanceToEmptySpaceAlongNegativeX(entity);
		
		if(Math.abs(leftDistance) < Math.abs(rightDistance))
		{
			return leftDistance;
		}
		return rightDistance;
	}
	
	private float yDistanceToMoveToResolveCollisions(Entity entity)
	{
		float upDistance = findDistanceToEmptySpaceAlongY(entity);;
		float downDistance = findDistanceToEmptySpaceAlongNegativeY(entity);
		
		if(Math.abs(upDistance) < Math.abs(downDistance))
		{
			return upDistance;
		}
		return downDistance;
	}
	
	private Vector2 xAndYDistanceToMoveToResolveCollisions(Entity entity)
	{
		Vector2 upAndRightDistance = findDistanceToEmptySpaceAlongXAndY(entity);
		Vector2 upAndLeftDistance = findDistanceToEmptySpaceAlongNegativeXAndY(entity);
		Vector2 downAndRightDistance = findDistanceToEmptySpaceAlongXAndNegativeY(entity);
		Vector2 downAndLeftDistance = findDistanceToEmptySpaceAlongNegativeXAndNegativeY(entity);
		
		float upAndRightTotal = Math.abs(upAndRightDistance.x) + Math.abs(upAndRightDistance.y);
		float upAndLeftTotal = Math.abs(upAndLeftDistance.x) + Math.abs(upAndLeftDistance.y);
		float downAndRightTotal = Math.abs(downAndRightDistance.x) + Math.abs(downAndRightDistance.y);
		float downAndLeftTotal = Math.abs(downAndLeftDistance.x) + Math.abs(downAndLeftDistance.y);
		
		if(upAndRightTotal < upAndLeftTotal && upAndRightTotal < downAndRightTotal && upAndRightTotal < downAndLeftTotal)
		{
			return upAndRightDistance;
		}
		else if(upAndLeftTotal < upAndRightTotal && upAndLeftTotal < downAndRightTotal && upAndLeftTotal < downAndLeftTotal)
		{
			return upAndLeftDistance;
		}
		else if(downAndRightTotal < upAndRightTotal && downAndRightTotal < upAndLeftTotal && downAndRightTotal < downAndLeftTotal)
		{
			return downAndRightDistance;
		}
		else
		{
			return downAndLeftDistance;
		}
	}
	
	private Vector2 findDistanceToEmptySpaceAlongXAndY(Entity entity)
	{
		Rectangle testHitbox = new Rectangle(Mappers.hitbox.get(entity).hitbox);
		boolean colliding = true;
		
		while(colliding)
		{
			colliding = false;
			for(Entity obstacle : obstacleEntities)
			{
				if(testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
				{
					testHitbox.x = Mappers.hitbox.get(obstacle).hitbox.x + Mappers.hitbox.get(obstacle).hitbox.width;
					testHitbox.y = Mappers.hitbox.get(obstacle).hitbox.y + Mappers.hitbox.get(obstacle).hitbox.height;
					colliding = true;
					break;
				}
			}
		}
		
		return new Vector2(testHitbox.x - Mappers.hitbox.get(entity).hitbox.x, testHitbox.y - Mappers.hitbox.get(entity).hitbox.y);
	}
	
	private Vector2 findDistanceToEmptySpaceAlongXAndNegativeY(Entity entity)
	{
		Rectangle testHitbox = new Rectangle(Mappers.hitbox.get(entity).hitbox);
		boolean colliding = true;
		
		while(colliding)
		{
			colliding = false;
			for(Entity obstacle : obstacleEntities)
			{
				if(testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
				{
					testHitbox.x = Mappers.hitbox.get(obstacle).hitbox.x + Mappers.hitbox.get(obstacle).hitbox.width;
					testHitbox.y = Mappers.hitbox.get(obstacle).hitbox.y - Mappers.hitbox.get(entity).hitbox.height;
					colliding = true;
					break;
				}
			}
		}
		
		return new Vector2(testHitbox.x - Mappers.hitbox.get(entity).hitbox.x, testHitbox.y - Mappers.hitbox.get(entity).hitbox.y);
	}
	
	private Vector2 findDistanceToEmptySpaceAlongNegativeXAndY(Entity entity)
	{
		Rectangle testHitbox = new Rectangle(Mappers.hitbox.get(entity).hitbox);
		boolean colliding = true;
		
		while(colliding)
		{
			colliding = false;
			for(Entity obstacle : obstacleEntities)
			{
				if(testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
				{
					testHitbox.x = Mappers.hitbox.get(obstacle).hitbox.x;
					testHitbox.y = Mappers.hitbox.get(obstacle).hitbox.y + Mappers.hitbox.get(obstacle).hitbox.height;
					
					colliding = true;
					break;
				}
			}
		}
		
		return new Vector2(testHitbox.x - Mappers.hitbox.get(entity).hitbox.x, testHitbox.y - Mappers.hitbox.get(entity).hitbox.y);
	}
	
	private Vector2 findDistanceToEmptySpaceAlongNegativeXAndNegativeY(Entity entity)
	{
		Rectangle testHitbox = new Rectangle(Mappers.hitbox.get(entity).hitbox);
		boolean colliding = true;
		
		while(colliding)
		{
			colliding = false;
			for(Entity obstacle : obstacleEntities)
			{
				if(testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
				{
					testHitbox.x = Mappers.hitbox.get(obstacle).hitbox.x;
					testHitbox.y = Mappers.hitbox.get(obstacle).hitbox.y - Mappers.hitbox.get(entity).hitbox.height;
					colliding = true;
					break;
				}
			}
		}
		
		return new Vector2(testHitbox.x - Mappers.hitbox.get(entity).hitbox.x, testHitbox.y - Mappers.hitbox.get(entity).hitbox.y);
	}
	
	private float findDistanceToEmptySpaceAlongX(Entity entity)
	{
		Rectangle testHitbox = new Rectangle(Mappers.hitbox.get(entity).hitbox);
		boolean colliding = true;
		
		while(colliding)
		{
			colliding = false;
			for(Entity obstacle : obstacleEntities)
			{
				if(testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
				{
					testHitbox.x = Mappers.hitbox.get(obstacle).hitbox.x + Mappers.hitbox.get(obstacle).hitbox.width;
					colliding = true;
					break;
				}
			}
		}
		
		return testHitbox.x - Mappers.hitbox.get(entity).hitbox.x;
	}
	
	private float findDistanceToEmptySpaceAlongNegativeX(Entity entity)
	{
		Rectangle testHitbox = new Rectangle(Mappers.hitbox.get(entity).hitbox);
		boolean colliding = true;
		
		while(colliding)
		{
			colliding = false;
			for(Entity obstacle : obstacleEntities)
			{
				if(testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
				{
					testHitbox.x = Mappers.hitbox.get(obstacle).hitbox.x - Mappers.hitbox.get(entity).hitbox.width;
					colliding = true;
					break;
				}
			}
		}
		
		return testHitbox.x - Mappers.hitbox.get(entity).hitbox.x;
	}
	
	private float findDistanceToEmptySpaceAlongY(Entity entity)
	{
		Rectangle testHitbox = new Rectangle(Mappers.hitbox.get(entity).hitbox);
		boolean colliding = true;
		
		while(colliding)
		{
			colliding = false;
			for(Entity obstacle : obstacleEntities)
			{
				if(testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
				{
					testHitbox.y = Mappers.hitbox.get(obstacle).hitbox.y + Mappers.hitbox.get(obstacle).hitbox.height;
					colliding = true;
					break;
				}
			}
		}
		
		return testHitbox.y - Mappers.hitbox.get(entity).hitbox.y;
	}
	
	private float findDistanceToEmptySpaceAlongNegativeY(Entity entity)
	{
		Rectangle testHitbox = new Rectangle(Mappers.hitbox.get(entity).hitbox);
		boolean colliding = true;
		
		while(colliding)
		{
			colliding = false;
			for(Entity obstacle : obstacleEntities)
			{
				if(testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
				{
					Gdx.app.log("[CollisionSystem]", "Testing -y resolution against " + Mappers.position.get(obstacle).x + ", " + Mappers.position.get(obstacle).y);
					testHitbox.y = Mappers.hitbox.get(obstacle).hitbox.y - Mappers.hitbox.get(entity).hitbox.height;
					colliding = true;
					break;
				}
			}
		}
		
		return testHitbox.y - Mappers.hitbox.get(entity).hitbox.y;
	}
}
