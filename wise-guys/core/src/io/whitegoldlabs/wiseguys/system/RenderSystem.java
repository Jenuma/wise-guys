package io.whitegoldlabs.wiseguys.system;

import java.util.Comparator;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.TypeComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class RenderSystem extends SortedIteratingSystem
{
	private final WiseGuys game;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public RenderSystem(final WiseGuys game)
	{
		super(Family.all(TypeComponent.class, SpriteComponent.class).get(), new TypeComparator());
		
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
	
	private static class TypeComparator implements Comparator<Entity>
	{
		@Override
		public int compare(Entity entityA, Entity entityB)
		{
			TypeComponent typeA = Mappers.type.get(entityA);
			TypeComponent typeB = Mappers.type.get(entityB);
			
			if(typeA.type == typeB.type)
			{
				return 0;
			}
			
			int valueA = 0;
			int valueB = 0;
			
			switch(typeA.type)
			{
				case PICKUP:
					valueA = 0;
					break;
				case EVENT:
					valueA = 1;
					break;
				case ENEMY_PROJECTILE:
					valueA = 2;
					break;
				case ENEMY:
					valueA = 3;
					break;
				case PLAYER_PROJECTILE:
					valueA = 4;
					break;
				case PLAYER:
					valueA = 5;
					break;
				case OBSTACLE:
					valueA = 6;
					break;
				case PRIORITY:
					valueA = 7;
					break;
			}
			
			switch(typeB.type)
			{
				case PICKUP:
					valueB = 0;
					break;
				case EVENT:
					valueB = 1;
					break;
				case ENEMY_PROJECTILE:
					valueB = 2;
					break;
				case ENEMY:
					valueB = 3;
					break;
				case PLAYER_PROJECTILE:
					valueB = 4;
					break;
				case PLAYER:
					valueB = 5;
					break;
				case OBSTACLE:
					valueB = 6;
					break;
				case PRIORITY:
					valueB = 7;
					break;
			}
			
			return valueA - valueB;
		}
	}
}
