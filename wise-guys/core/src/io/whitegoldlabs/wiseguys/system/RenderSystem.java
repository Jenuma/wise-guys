package io.whitegoldlabs.wiseguys.system;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class RenderSystem extends SortedIteratingSystem
{
	private final WiseGuys game;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public RenderSystem(final WiseGuys game, Family family, Comparator<Entity> comparator)
	{
		super(family, comparator);
		
		this.game = game;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.begin();
		
		float x = Mappers.position.get(entity).x;
		float y = Mappers.position.get(entity).y;
		SpriteComponent sprite = Mappers.sprite.get(entity);
		
		sprite.sprite.setPosition(x, y);
		sprite.sprite.draw(game.batch);
		
		game.batch.end();
	}
}
