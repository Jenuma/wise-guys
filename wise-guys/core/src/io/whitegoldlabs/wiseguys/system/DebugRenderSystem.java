package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class DebugRenderSystem extends EntitySystem
{
	private ImmutableArray<Entity> hitboxEntities;
	
	private final WiseGuys game;
	
	private SpriteBatch debugBatch;
	private OrthographicCamera camera;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public DebugRenderSystem(final WiseGuys game, SpriteBatch debugBatch, OrthographicCamera camera)
	{
		this.game = game;
		
		this.debugBatch = debugBatch;
		this.camera = camera;
	}
	
	public void addedToEngine(Engine engine)
	{
		hitboxEntities = engine.getEntitiesFor(Family.all
		(
			HitboxComponent.class
		).get());
	}
	
	public void update(float deltaTime)
	{
		// Debug Hitboxes
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		
		for(Entity entity : hitboxEntities)
		{
			HitboxComponent hitboxComponent = Mappers.hitbox.get(entity);
        	Sprite hitboxSprite = hitboxComponent.sprite;
        	hitboxSprite.setPosition(hitboxComponent.hitbox.x, hitboxComponent.hitbox.y);
        	hitboxSprite.draw(game.batch);
		}
		
		game.batch.end();
		
		PositionComponent playerPosition = Mappers.position.get(game.player);
		VelocityComponent playerVelocity = Mappers.velocity.get(game.player);
		AccelerationComponent playerAcceleration = Mappers.acceleration.get(game.player);
		StateComponent playerMovingState = Mappers.state.get(game.player);
		
		// Debug Player Attributes
		debugBatch.begin();
		game.font.draw(debugBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, 650);
		game.font.draw(debugBatch, "Pos: " + playerPosition.x + ", " + playerPosition.y, 5, 630);
		game.font.draw(debugBatch, "Vel: " + playerVelocity.x + ", " + playerVelocity.y, 5, 610);
		game.font.draw(debugBatch, "Accel: " + playerAcceleration.x + ", " + playerAcceleration.y, 5, 590);
		game.font.draw(debugBatch, "Player State: " + playerMovingState.airborneState
    		+ ", " + playerMovingState.motionState
    		+ ", " + playerMovingState.directionState, 5, 570);
        debugBatch.end();
	}
}
