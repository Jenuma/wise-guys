package io.whitegoldlabs.wiseguys.model;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PickupComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.TypeComponent;
import io.whitegoldlabs.wiseguys.util.Assets;

public class World
{
	private Array<Array<Entity>> worldEntities;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public World(String fileName)
	{
		this.worldEntities = new Array<Array<Entity>>();
		this.worldEntities.add(loadWorldEntitiesFromFile(fileName));
		
		// Get sub-worlds
		int i = 1;
		while(Gdx.files.internal(fileName + "sub" + i + ".csv").exists())
		{
			this.worldEntities.add(loadWorldEntitiesFromFile(fileName + "sub" + i));
			i++;
		}
	}
	
	public Array<Array<Entity>> getWorldEntities()
	{
		return this.worldEntities;
	}
	
	private Array<Entity> loadWorldEntitiesFromFile(String fileName)
	{
		Array<Entity> entities = new Array<>();
		
		Entity entity;
		Sprite hitboxSprite = null;
		TypeComponent.Type type = TypeComponent.Type.OBSTACLE;
		
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
					
					// GOAL TOP
					if(cells[x].equals("59"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// COIN
					else if(cells[x].equals("7"))
					{
						hitboxSprite = new Sprite(Assets.spriteSheet, 112, 144, 10, 16);
						type = TypeComponent.Type.PICKUP;
						entity.add(new PickupComponent(PickupComponent.Pickup.COIN));
					}
					// GOAL
					else if(cells[x].equals("69"))
					{
						hitboxSprite = null;
						type = TypeComponent.Type.EVENT;
					}
					// OPEN BATTLEMENTS
					else if(cells[x].equals("60"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// CASTLE WINDOW
					else if(cells[x].equals("61"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// CASTLE BRICKS
					else if(cells[x].equals("62"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// CASTLE WINDOW 2
					else if(cells[x].equals("63"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// ClOSED BATTLEMENTS
					else if(cells[x].equals("70"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// CASTLE DOOR TOP
					else if(cells[x].equals("71"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// CASTLE DOOR
					else if(cells[x].equals("72"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// UP PIPE 1
					else if(cells[x].equals("75"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// UP PIPE 2
					else if(cells[x].equals("76"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// UP PIPE 3
					else if(cells[x].equals("85"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// UP PIPE 4
					else if(cells[x].equals("86"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// LEFT PIPE 1
					else if(cells[x].equals("77"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// LEFT PIPE 2
					else if(cells[x].equals("78"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// LEFT PIPE 3
					else if(cells[x].equals("79"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// LEFT PIPE 4
					else if(cells[x].equals("87"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// LEFT PIPE 5
					else if(cells[x].equals("88"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// LEFT PIPE 6
					else if(cells[x].equals("89"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// BLOCK
					else if(cells[x].equals("80"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// GROUND
					else if(cells[x].equals("81"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// BOX
					else if(cells[x].equals("50"))
					{
						entity.add(new StateComponent());
						
						Array<Sprite> boxStillSprites = new Array<>();
						boxStillSprites.add(new Sprite(Assets.spriteSheet, 0, 80, 16, 16));
						boxStillSprites.add(new Sprite(Assets.spriteSheet, 16, 80, 16, 16));
						boxStillSprites.add(new Sprite(Assets.spriteSheet, 32, 80, 16, 16));
						
						AnimationComponent ac = new AnimationComponent();
						ac.animations.put("STILL", new Animation<Sprite>(1f/4f, boxStillSprites, Animation.PlayMode.LOOP_PINGPONG));
						entity.add(ac);
						
						type = TypeComponent.Type.OBSTACLE;
					}
					// BRICKS TOP
					else if(cells[x].equals("83"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					// BRICKS
					else if(cells[x].equals("84"))
					{
						type = TypeComponent.Type.OBSTACLE;
					}
					else
					{
						hitboxSprite = null;
						type = TypeComponent.Type.OBSTACLE;
					}
				
					if(spriteX != -1 && spriteY != -1)
					{
						entity.add(new SpriteComponent(new Sprite(Assets.spriteSheet, spriteX, spriteY, 16, 16)));
					}
					
					if(type == TypeComponent.Type.OBSTACLE)
					{
						hitboxSprite = new Sprite(Assets.spriteSheet, 128, 144, 16, 16);
					}
					
					if(hitboxSprite != null)
					{
						entity.add(new HitboxComponent
						(
							x*16,
							(csvLines.length-1-y)*16,
							hitboxSprite.getWidth(),
							hitboxSprite.getHeight(),
							hitboxSprite
						));
					}
					
					entity.add(new TypeComponent(type));
					entities.add(entity);
				}
			}
		}
		
		return entities;
	}
}
