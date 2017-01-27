package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.CollisionComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class CollisionSystem extends EntitySystem
{
	private ImmutableArray<Entity> scriptedEntities;
	private ImmutableArray<Entity> hitboxEntities;
	
	private final WiseGuys game;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public CollisionSystem(final WiseGuys game)
	{
		this.game = game;
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		scriptedEntities = engine.getEntitiesFor(Family
				.all(ScriptComponent.class)
		.get());
		
		hitboxEntities = engine.getEntitiesFor(Family
				.all(HitboxComponent.class)
		.get());
	}
	
	// ---------------------------------------------------------------------------------|
	// Update                                                                           |
	// ---------------------------------------------------------------------------------|
	@Override
	public void update(float deltaTime)
	{
		if(game.isRunning)
		{
			// Handle all non-player collisions.
			for(int i = 0; i < scriptedEntities.size(); i++)
			{
				while(isColliding(scriptedEntities.get(i)))
				{
					resolveCollision(scriptedEntities.get(i));
				}
			}
			
			// Handle player collisions.
			while(isColliding(game.player))
			{
				resolveCollision(game.player);
			}
		}
	}
	
	// ---------------------------------------------------------------------------------|
	// Private Methods                                                                  |
	// ---------------------------------------------------------------------------------|
	private boolean isColliding(Entity entity)
	{	
		boolean colliding = false;
		
		// Test scripted entities against everything except the player.
		if(entity != game.player)
		{
			for(int i = 0; i < hitboxEntities.size(); i++)
			{
				// Ignore the collision if they are the same entity.
				if(entity == hitboxEntities.get(i))
				{
					continue;
				}
				
				// If the scripted entity is colliding with something, queue its script.
				if(Mappers.hitbox.get(entity).hitbox.overlaps(Mappers.hitbox.get(hitboxEntities.get(i)).hitbox))
				{
					entity.add(new CollisionComponent(hitboxEntities.get(i)));
					game.scriptManager.queueScriptToExecute(Mappers.script.get(entity));
					
					// Don't bother trying to resolve collision if entity doesn't move!
					if(!Mappers.velocity.has(entity))
					{
						continue;
					}
					
					if(Mappers.script.get(entity).collidable)
					{
						// Check if other entity is scripted.
						if(Mappers.script.has(hitboxEntities.get(i)))
						{
							// If both entities are collidable, resolve.
							if(Mappers.script.get(hitboxEntities.get(i)).collidable)
							{
								colliding = true;
							}
						}
						// If this entity is collidable and the other isn't scripted, resolve.
						else
						{
							colliding = true;
						}
					}
				}
			}
			
			return colliding;
		}
		// Test the player against hitbox entities.
		else
		{
			for(int i = 0; i < hitboxEntities.size(); i++)
			{
				// Make sure player is not checking itself.
				if(entity != hitboxEntities.get(i))
				{
					if(Mappers.hitbox.get(entity).hitbox.overlaps(Mappers.hitbox.get(hitboxEntities.get(i)).hitbox))
					{
						// Check if other entity is scripted.
						if(Mappers.script.has(hitboxEntities.get(i)))
						{
							// If the other entity is collidable, resolve.
							if(Mappers.script.get(hitboxEntities.get(i)).collidable)
							{
								return true;
							}
						}
						// If the other entity isn't scripted, resolve.
						else
						{
							return true;
						}
					}
				}
			}
			
			return false;
		}
		
		//////////////
//		if(Mappers.script.has(entity))
//		{
//			if(!Mappers.script.get(entity).collidable)
//			{
//				return false;
//			}
//		}
//		
//		boolean collidable;
//		
//		for(int i = 0; i < hitboxEntities.size(); i++)
//		{
//			Entity otherEntity = hitboxEntities.get(i);
//			if(entity != otherEntity)
//			{
//				collidable = true;
//				
//				if(Mappers.script.has(otherEntity))
//				{
//					collidable = Mappers.script.get(otherEntity).collidable;
//				}
//				
//				if(otherEntity == game.player)
//				{
//					collidable = false;
//				}
//				
//				if(Mappers.hitbox.get(entity).hitbox.overlaps(Mappers.hitbox.get(otherEntity).hitbox) && collidable)
//				{
//					return true;
//				}
//			}
//		}
//		
//		return false;
	}
	
	private void resolveCollision(Entity entity)
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
				Mappers.state.get(entity).airborneState = StateComponent.AirborneState.GROUNDED;
			}
			
			Mappers.position.get(entity).y += yDistanceToResolve;
			Mappers.hitbox.get(entity).hitbox.y += yDistanceToResolve;
			Mappers.velocity.get(entity).y = 0;
		}
		else
		{
			if(xAndYDistanceToResolve.y > 0)
			{
				Mappers.state.get(entity).airborneState = StateComponent.AirborneState.GROUNDED;
			}
			
			Mappers.position.get(entity).x += xAndYDistanceToResolve.x;
			Mappers.position.get(entity).y += xAndYDistanceToResolve.y;
			Mappers.hitbox.get(entity).hitbox.x += xAndYDistanceToResolve.x;
			Mappers.hitbox.get(entity).hitbox.y += xAndYDistanceToResolve.y;
		}
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
			for(Entity obstacle : hitboxEntities)
			{
				if(entity != obstacle && testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
				{
//					testHitbox.x = Mappers.hitbox.get(obstacle).hitbox.x + Mappers.hitbox.get(obstacle).hitbox.width;
//					testHitbox.y = Mappers.hitbox.get(obstacle).hitbox.y + Mappers.hitbox.get(obstacle).hitbox.height;
					testHitbox.x = Mappers.hitbox.get(obstacle).hitbox.x + testHitbox.width;
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
			for(Entity obstacle : hitboxEntities)
			{
				if(entity != obstacle && testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
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
			for(Entity obstacle : hitboxEntities)
			{
				if(entity != obstacle && testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
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
			for(Entity obstacle : hitboxEntities)
			{
				if(entity != obstacle && testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
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
			for(Entity obstacle : hitboxEntities)
			{
				if(entity != obstacle && testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
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
			for(Entity obstacle : hitboxEntities)
			{
				if(entity != obstacle && testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
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
			for(Entity obstacle : hitboxEntities)
			{
				if(entity != obstacle && testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
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
			for(Entity obstacle : hitboxEntities)
			{
				if(entity != obstacle && testHitbox.overlaps(Mappers.hitbox.get(obstacle).hitbox))
				{
					testHitbox.y = Mappers.hitbox.get(obstacle).hitbox.y - Mappers.hitbox.get(entity).hitbox.height;
					colliding = true;
					break;
				}
			}
		}
		
		return testHitbox.y - Mappers.hitbox.get(entity).hitbox.y;
	}
}
