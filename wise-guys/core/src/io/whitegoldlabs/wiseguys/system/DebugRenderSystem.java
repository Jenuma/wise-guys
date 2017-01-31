package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PlayerComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class DebugRenderSystem extends EntitySystem
{
	private ImmutableArray<Entity> hitboxEntities;
	
	private final WiseGuys game;
	
	private SpriteBatch debugBatch;
	private ShapeRenderer shapeRenderer;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public DebugRenderSystem(final WiseGuys game, SpriteBatch debugBatch)
	{
		this.game = game;
		
		this.debugBatch = debugBatch;
		this.shapeRenderer = new ShapeRenderer();
	}
	
	@Override
	public void addedToEngine(Engine engine)
	{
		hitboxEntities = engine.getEntitiesFor(Family.all
		(
			HitboxComponent.class
		).get());
	}
	
	@Override
	public void update(float deltaTime)
	{
		// Debug Hitboxes
		game.camera.update();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shapeRenderer.setProjectionMatrix(game.camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		for(Entity entity : hitboxEntities)
		{
			if(Mappers.type.has(entity))
			{
				switch(Mappers.type.get(entity).type)
				{
					case PICKUP:
						shapeRenderer.setColor(0, 0, 1, 0.5f);
						break;
					case EVENT:
						shapeRenderer.setColor(1, 0, 1, 0.5f);
						break;
					case ENEMY_PROJECTILE:
					case ENEMY:
						shapeRenderer.setColor(1, 0, 1, 0.5f);
						break;
					case PLAYER_PROJECTILE:
					case PLAYER:
						shapeRenderer.setColor(0, 1, 0, 0.5f);
						break;
					case OBSTACLE:
						shapeRenderer.setColor(0, 0, 0, 0.5f);
				}
			}
			
			Rectangle hitbox = Mappers.hitbox.get(entity).hitbox;
    		shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		}
		
		shapeRenderer.end();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		PositionComponent playerPosition = Mappers.position.get(game.player);
		VelocityComponent playerVelocity = Mappers.velocity.get(game.player);
		AccelerationComponent playerAcceleration = Mappers.acceleration.get(game.player);
		StateComponent playerMovingState = Mappers.state.get(game.player);
		PlayerComponent playerComponent = Mappers.player.get(game.player);
		
		// Debug Player Attributes
		debugBatch.begin();
		game.font.draw(debugBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, 650);
		game.font.draw(debugBatch, "Pos: " + playerPosition.x + ", " + playerPosition.y, 5, 630);
		game.font.draw(debugBatch, "Vel: " + playerVelocity.x + ", " + playerVelocity.y, 5, 610);
		game.font.draw(debugBatch, "Accel: " + playerAcceleration.x + ", " + playerAcceleration.y, 5, 590);
		game.font.draw(debugBatch, "State: " + playerMovingState.airborneState
    		+ ", " + playerMovingState.motionState
    		+ ", " + playerMovingState.directionState
    		+ ", " + playerComponent.playerState, 5, 570);
		game.font.draw(debugBatch, "Event Processing: " + game.eventProcessing, 5, 550);
        debugBatch.end();
	}
}
