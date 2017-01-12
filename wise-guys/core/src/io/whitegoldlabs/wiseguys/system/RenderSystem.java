package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;

public class RenderSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	ComponentMapper<PositionComponent> pMap = ComponentMapper.getFor(PositionComponent.class);
	ComponentMapper<SpriteComponent> sMap = ComponentMapper.getFor(SpriteComponent.class);
	
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
		entities = engine.getEntitiesFor(Family.all(PositionComponent.class, SpriteComponent.class).get());
	}
	
	public void update(float deltaTime)
	{
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		for(int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);
			float x = pMap.get(entity).x;
			float y = pMap.get(entity).y;
			SpriteComponent sprite = sMap.get(entity);
			
			sprite.sprite.setPosition(x, y);
			sprite.sprite.draw(batch);
		}
		batch.end();
	}
}
