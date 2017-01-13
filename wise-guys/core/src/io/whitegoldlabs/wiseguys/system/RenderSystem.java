package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class RenderSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public RenderSystem(SpriteBatch batch, OrthographicCamera camera)
	{
		this.batch = batch;
		this.camera = camera;
	}
	
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.all
		(
			PositionComponent.class,
			SpriteComponent.class
		).get());
	}
	
	public void update(float deltaTime)
	{
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for(Entity entity : entities)
		{
			float x = Mappers.position.get(entity).x;
			float y = Mappers.position.get(entity).y;
			SpriteComponent sprite = Mappers.sprite.get(entity);
			
			sprite.sprite.setPosition(x, y);
			sprite.sprite.draw(batch);
		}
		
		batch.end();
	}
}
