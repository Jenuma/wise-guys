package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class DebugRenderSystem extends EntitySystem
{
	private ImmutableArray<Entity> hitboxEntities;
	
	private SpriteBatch hitboxBatch;
	private SpriteBatch debugBatch;
	private OrthographicCamera camera;
	private BitmapFont font;
	private Entity player;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public DebugRenderSystem(SpriteBatch hitboxBatch, SpriteBatch debugBatch, OrthographicCamera camera, BitmapFont font, Entity player)
	{
		this.hitboxBatch = hitboxBatch;
		this.debugBatch = debugBatch;
		this.camera = camera;
		this.font = font;
		this.player = player;
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
		hitboxBatch.setProjectionMatrix(camera.combined);
		hitboxBatch.begin();
		
		for(Entity entity : hitboxEntities)
		{
			HitboxComponent hitboxComponent = Mappers.hitbox.get(entity);
        	Sprite hitboxSprite = hitboxComponent.sprite;
        	hitboxSprite.setPosition(hitboxComponent.hitbox.x, hitboxComponent.hitbox.y);
        	hitboxSprite.draw(hitboxBatch);
		}
		
		hitboxBatch.end();
		
		PositionComponent playerPosition = Mappers.position.get(player);
		VelocityComponent playerVelocity = Mappers.velocity.get(player);
		AccelerationComponent playerAcceleration = Mappers.acceleration.get(player);
		StateComponent playerMovingState = Mappers.movingState.get(player);
		
		// Debug Player Attributes
		debugBatch.begin();
        font.draw(debugBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, 650);
        font.draw(debugBatch, "Pos: " + playerPosition.x + ", " + playerPosition.y, 5, 630);
        font.draw(debugBatch, "Vel: " + playerVelocity.x + ", " + playerVelocity.y, 5, 610);
        font.draw(debugBatch, "Accel: " + playerAcceleration.x + ", " + playerAcceleration.y, 5, 590);
        font.draw(debugBatch, "Player State: " + playerMovingState.airborneState
    		+ ", " + playerMovingState.motionState
    		+ ", " + playerMovingState.directionState, 5, 570);
        debugBatch.end();
	}
}
