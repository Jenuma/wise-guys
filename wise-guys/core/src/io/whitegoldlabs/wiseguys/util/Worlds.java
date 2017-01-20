package io.whitegoldlabs.wiseguys.util;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;

public class Worlds
{
	// ---------------------------------------------------------------------------------|
	// Fields                                                                           |
	// ---------------------------------------------------------------------------------|
	private static ArrayMap<String, Array<Entity>> worldEntities = new ArrayMap<String, Array<Entity>>();
	
	// ---------------------------------------------------------------------------------|
	// getWorld                                                                         |
	// ---------------------------------------------------------------------------------|
	public static Array<Entity> getWorld(WiseGuys game, OrthographicCamera camera, Entity player, Engine engine, String worldName)
	{
		if(!worldEntities.containsKey(worldName))
		{
			worldEntities.put(worldName, loadWorldEntitiesFromFile(game, camera, player, engine, worldName));
		}
		
		return worldEntities.get(worldName);
	}
	
	// ---------------------------------------------------------------------------------|
	// loadWorldEntitiesFromFile                                                        |
	// ---------------------------------------------------------------------------------|
	private static Array<Entity> loadWorldEntitiesFromFile(WiseGuys game, OrthographicCamera camera, Entity player, Engine engine, String fileName)
	{
		Array<Entity> entities = new Array<>();
		Entity entity;
		
		StateComponent state;
		Sprite hitboxSprite = null;
		
		boolean hasSprite = true;
		int spriteX = -1;
		int spriteY = -1;
		
		String[] csvLines = Gdx.files.internal(fileName + ".csv").readString().split("\r\n");
		
		for(int y = csvLines.length - 1; y >= 0; y--)
		{
			String[] cells = csvLines[y].split(",");
			for(int x = 0; x < cells.length; x++)
			{
				if(!cells[x].equals("-1"))
				{
					entity = new Entity();
					entity.add(new PositionComponent(x*16, (csvLines.length-1-y)*16));
					state = new StateComponent();
					entity.add(state);
				
					// Calculate the position of the entity's sprite on the sprite sheet
					if(cells[x].length() == 2)
					{
						spriteX = 16 * Integer.parseInt(cells[x].substring(1));
						spriteY = 16 * Integer.parseInt(cells[x].substring(0, 1));
					}
					else
					{
						spriteX = 16 * Integer.parseInt(cells[x]);
						spriteY = 0;
					}
					
					// COIN
					if(cells[x].equals("7"))
					{
						hitboxSprite = new Sprite(Assets.spriteSheet, 112, 144, 16, 16);
						entity.add(new HitboxComponent
						(
							x*16+3,
							(csvLines.length-1-y)*16,
							10,
							hitboxSprite.getHeight(),
							hitboxSprite
						));
						
						Object[] args = new Object[4];
						args[0] = Mappers.inventory.get(player);
						args[1] = state;
						args[2] = StateComponent.EnabledState.DISABLED;
						args[3] = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
						
						entity.add(new ScriptComponent("coin.lua", args));
					}
					// BOX
					else if(cells[x].equals("50"))
					{
						Array<Sprite> boxStillSprites = new Array<>();
						boxStillSprites.add(new Sprite(Assets.spriteSheet, 0, 80, 16, 16));
						boxStillSprites.add(new Sprite(Assets.spriteSheet, 16, 80, 16, 16));
						boxStillSprites.add(new Sprite(Assets.spriteSheet, 32, 80, 16, 16));
						
						AnimationComponent ac = new AnimationComponent();
						ac.animations.put("STILL", new Animation<Sprite>(1f/4f, boxStillSprites, Animation.PlayMode.LOOP_PINGPONG));
						entity.add(ac);
					}
					// TELEPORT
					else if(cells[x].equals("90"))
					{
						hasSprite = false;
						
						hitboxSprite = new Sprite(Assets.spriteSheet, 96, 144, 16, 16);
						entity.add(new HitboxComponent
						(
							x*16,
							(csvLines.length-1-y)*16,
							hitboxSprite.getWidth(),
							1,
							hitboxSprite
						));
						
						Object[] args = new Object[4];
						args[0] = game;
						args[1] = camera;
						args[2] = player;
						args[3] = engine;
						
						entity.add(new ScriptComponent("teleport.lua", args));
					}
					else
					{
						hasSprite = true;
						hitboxSprite = null;
					}
				
					// Get sprite from spriteSheet
					if(hasSprite)
					{
						entity.add(new SpriteComponent(new Sprite(Assets.spriteSheet, spriteX, spriteY, 16, 16)));
					}
					
					// Obstacle default hitbox and hitbox sprite
					if(hitboxSprite == null)
					{
						hitboxSprite = new Sprite(Assets.spriteSheet, 128, 144, 16, 16);
						entity.add(new HitboxComponent
						(
							x*16,
							(csvLines.length-1-y)*16,
							hitboxSprite.getWidth(),
							hitboxSprite.getHeight(),
							hitboxSprite
						));
					}
					
					entities.add(entity);
				}
			}
		}
		
		return entities;
	}
}
