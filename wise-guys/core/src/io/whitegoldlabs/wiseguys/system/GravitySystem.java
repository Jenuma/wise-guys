package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class GravitySystem extends EntitySystem
{
	private ImmutableArray<Entity> dynamicEntities;
	private ImmutableArray<Entity> obstacleEntities;
	
	private final WiseGuys game;
	private final float G = -0.6f;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public GravitySystem(final WiseGuys game)
	{
		this.game = game;
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		dynamicEntities = engine.getEntitiesFor(Family.all
		(
				StateComponent.class,
				VelocityComponent.class,
				AccelerationComponent.class,
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
	
	@Override
	public void update(float deltaTime)
	{
		if(game.isRunning)
		{
			for(Entity dynamicEntity : dynamicEntities)
			{
				VelocityComponent velocity = Mappers.velocity.get(dynamicEntity);
				AccelerationComponent acceleration = Mappers.acceleration.get(dynamicEntity);
				StateComponent state = Mappers.state.get(dynamicEntity);
				
				// If the entity is falling, apply gravity.
				if(state.airborneState == StateComponent.AirborneState.FALLING ||
					state.airborneState == StateComponent.AirborneState.JUMPING)
				{
					acceleration.y = G;
				}
				// If the entity isn't falling, check to see if it should be.
				else
				{
					velocity.y = 0;
					acceleration.y = 0;
					
					Rectangle fallbox = new Rectangle(Mappers.hitbox.get(dynamicEntity).hitbox);
					fallbox.y--;
					
					boolean grounded = false;
					for(Entity obstacle : obstacleEntities)
					{
						Rectangle obstacleHitbox = Mappers.hitbox.get(obstacle).hitbox;
						if(fallbox.overlaps(obstacleHitbox) && !Mappers.phase.has(obstacle))
						{
							grounded = true;
							break;
						}
					}
					
					if(!grounded)
					{
						Gdx.app.log("[GRAVITY]", "Entity " + dynamicEntity + " determined to be falling");
						state.airborneState = StateComponent.AirborneState.FALLING;
					}
				}
			}
		}
	}
}
