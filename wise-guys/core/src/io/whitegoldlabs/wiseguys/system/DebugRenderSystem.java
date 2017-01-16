package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.whitegoldlabs.wiseguys.component.CollectboxComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class DebugRenderSystem extends EntitySystem
{
	private ImmutableArray<Entity> hitboxEntities;
	private ImmutableArray<Entity> collectboxEntities;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public DebugRenderSystem(SpriteBatch batch, OrthographicCamera camera)
	{
		this.batch = batch;
		this.camera = camera;
	}
	
	public void addedToEngine(Engine engine)
	{
		hitboxEntities = engine.getEntitiesFor(Family.all
		(
			HitboxComponent.class
		).get());
		
		collectboxEntities = engine.getEntitiesFor(Family.all
		(
			CollectboxComponent.class
		).get());
	}
	
	public void update(float deltaTime)
	{
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for(Entity entity : hitboxEntities)
		{
			HitboxComponent hitboxComponent = Mappers.hitbox.get(entity);
        	Sprite hitboxSprite = hitboxComponent.sprite;
        	hitboxSprite.setPosition(hitboxComponent.hitbox.x, hitboxComponent.hitbox.y);
        	hitboxSprite.draw(batch);
		}
		
		for(Entity entity : collectboxEntities)
		{
			CollectboxComponent collectboxComponent = Mappers.collectbox.get(entity);
        	Sprite collectboxSprite = collectboxComponent.sprite;
        	collectboxSprite.setPosition(collectboxComponent.collectbox.x, collectboxComponent.collectbox.y);
        	collectboxSprite.draw(batch);
		}
		
		batch.end();
	}
}
