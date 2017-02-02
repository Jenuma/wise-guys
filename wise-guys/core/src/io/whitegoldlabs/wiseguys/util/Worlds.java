package io.whitegoldlabs.wiseguys.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PhaseComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.TypeComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;

public class Worlds
{
	private static ArrayMap<String, Array<Entity>> worldEntities = new ArrayMap<String, Array<Entity>>();
	
	// ---------------------------------------------------------------------------------|
	// getWorld                                                                         |
	// ---------------------------------------------------------------------------------|
	public static Array<Entity> getWorld(WiseGuys game, String worldName)
	{
		if(!worldEntities.containsKey(worldName))
		{
			worldEntities.put(worldName, loadWorldFromFile(game, worldName));
		}
		
		return worldEntities.get(worldName);
	}
	
	// ---------------------------------------------------------------------------------|
	// getLoadedWorlds                                                                  |
	// ---------------------------------------------------------------------------------|
	public static Array<String> getLoadedWorlds()
	{
		Array<String> worldNames = new Array<>();
		
		for(String worldName : worldEntities.keys())
		{
			worldNames.add(worldName.toString());
		}
		
		return worldNames;
	}
	
	// ---------------------------------------------------------------------------------|
	// unloadWorldEntities                                                              |
	// ---------------------------------------------------------------------------------|
	public static void unloadWorldEntities()
	{
		worldEntities.clear();
	}
	
	// ---------------------------------------------------------------------------------|
	// loadWorldEntitiesFromFile                                                        |
	// ---------------------------------------------------------------------------------|
	private static Array<Entity> loadWorldFromFile(WiseGuys game, String worldName)
	{
		game.assets.load();
		game.assets.manager.finishLoading();
		
		Array<Entity> entities = new Array<>();
	    Entity entity;
	    Entity object;
		
		ArrayMap<String, Integer> keyMap = new ArrayMap<>();;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = null;
	    Document doc = null;
		
		try
		{
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(worldName + ".tmx");
		}
		catch (Exception ex) {}
		
	    doc.getDocumentElement().normalize();

	    float mapWidth = Float.parseFloat(doc.getDocumentElement().getAttribute("width"));
	    float mapHeight = Float.parseFloat(doc.getDocumentElement().getAttribute("height"));
	    float mapPixelHeight = mapHeight * 16;
	    
	    Element tileElement = (Element)doc.getElementsByTagName("layer").item(0);
	    String[] tiles = tileElement.getTextContent().trim().split(",");
    	
	    // -------------------------------------------------------------------|
	    // Tiles                                                              |
	    // -------------------------------------------------------------------|
	    int tileX = -1;
	    int tileY = -1;
	    
	    for(int i = 0; i < tiles.length; i++)
	    {
	    	int tile = Integer.parseInt(tiles[i].trim()) - 1;
	    	String tileStr = String.valueOf(tile);
	    	
	    	tileX = i % (int)mapWidth;
	    	
	    	if(tileX % mapWidth == 0)
	    	{
	    		tileY++;
	    	}
	    	
	    	Texture spriteSheet = game.assets.manager.get(Assets.spriteSheet);
			
			StateComponent state;
			
			boolean hasSprite = true;
			
			int spriteX = -1;
			int spriteY = -1;
	    	
	    	if(tile != -1)
			{
				entity = new Entity();
				entity.add(new PositionComponent(tileX*16, (mapHeight-1-tileY)*16));
				state = new StateComponent();
				entity.add(state);
			
				// Calculate the position of the entity's sprite on the sprite sheet
				if(tileStr.length() == 2)
				{
					spriteX = 16 * Integer.parseInt(tileStr.substring(1));
					spriteY = 16 * Integer.parseInt(tileStr.substring(0, 1));
				}
				else
				{
					spriteX = 16 * Integer.parseInt(tileStr);
					spriteY = 0;
				}
				
				// Goomba
				if(tile == 30)
				{
					entity.add(new TypeComponent(TypeComponent.Type.ENEMY));
					entity.add(new HitboxComponent(tileX*16, (mapHeight-1-tileY)*16, 16, 16));
					
					entity.add(new VelocityComponent(0.8f, 0));
					entity.add(new AccelerationComponent(0, 0));
					entity.add(new PhaseComponent());
					
					state.motionState = StateComponent.MotionState.MOVING;
					state.airborneState = StateComponent.AirborneState.GROUNDED;
					state.directionState = StateComponent.DirectionState.RIGHT;
					
					Array<Sprite> movingSprites = new Array<>();
					movingSprites.add(new Sprite(spriteSheet, 0, 48, 16, 16));
					movingSprites.add(new Sprite(spriteSheet, 16, 48, 16, 16));
					
					AnimationComponent ac = new AnimationComponent();
					ac.animations.put("MOVING", new Animation<Sprite>(1f/4f, movingSprites, Animation.PlayMode.LOOP));
					entity.add(ac);
					
					Array<Object> args = new Array<>();
					args.add(entity);
					args.add(game);
					
					ScriptComponent script = new ScriptComponent("scripts\\goomba.lua", args);
					entity.add(script);
				}
				// COIN
				else if(tile == 7)
				{
					entity.add(new TypeComponent(TypeComponent.Type.PICKUP));
					entity.add(new HitboxComponent(tileX*16+3, (mapHeight-1-tileY)*16, 10, 16));
					entity.add(new PhaseComponent());
					
					Array<Object> args = new Array<>();
					args.add(entity);
					args.add(game);
					
					entity.add(new ScriptComponent("scripts\\coin.lua", args));
				}
				// BOX
				else if(tile == 50)
				{
					entity.add(new TypeComponent(TypeComponent.Type.OBSTACLE));
					entity.add(new HitboxComponent(tileX*16, (mapHeight-1-tileY)*16, 16, 16));
					
					Array<Sprite> boxStillSprites = new Array<>();
					boxStillSprites.add(new Sprite(spriteSheet, 0, 80, 16, 16));
					boxStillSprites.add(new Sprite(spriteSheet, 16, 80, 16, 16));
					boxStillSprites.add(new Sprite(spriteSheet, 32, 80, 16, 16));
					
					AnimationComponent animationComponent = new AnimationComponent();
					animationComponent.animations.put("STILL", new Animation<Sprite>(1f/4f, boxStillSprites, Animation.PlayMode.LOOP_PINGPONG));
					entity.add(animationComponent);
				
					Array<Object> args = new Array<>();
					args.add(entity);
					args.add(game);
					args.add(new Sprite(spriteSheet, 48, 80, 16, 16));
					args.add(new Sprite(spriteSheet, 128, 0, 16, 16));
					
					entity.add(new ScriptComponent("scripts\\box.lua", args));
				}
				// GOAL TOP
				else if(tile == 59)
				{
					entity.add(new TypeComponent(TypeComponent.Type.EVENT));
				}
				// GOAL
				else if(tile == 69)
				{
					entity.add(new TypeComponent(TypeComponent.Type.EVENT));
				}
				// BRICKS
				else if(tile == 83)
				{
					entity.add(new TypeComponent(TypeComponent.Type.OBSTACLE));
					entity.add(new HitboxComponent(tileX*16, (mapHeight-1-tileY)*16, 16, 16));
					
					Array<Object> args = new Array<>();
					args.add(entity);
					args.add(game);
					
					entity.add(new ScriptComponent("scripts\\bricks.lua", args));
				}
				else
				{
					entity.add(new TypeComponent(TypeComponent.Type.OBSTACLE));
					hasSprite = true;
				}
				
				// Get sprite from spriteSheet
				if(hasSprite && !Mappers.sprite.has(entity))
				{
					entity.add(new SpriteComponent(new Sprite(spriteSheet, spriteX, spriteY, 16, 16)));
				}
				
				entities.add(entity);
			}
	    }
	    
	    // -------------------------------------------------------------------|
	    // Objects                                                            |
	    // -------------------------------------------------------------------|
	    NodeList objectList = doc.getElementsByTagName("object");
        for(int i = 0; i < objectList.getLength(); i++)
        {
            Node objectNode = objectList.item(i);
            if(objectNode.getNodeType() == Node.ELEMENT_NODE)
            {
            	object = new Entity();
                Element objectElement = (Element)objectNode;
                
                float objectX = Float.parseFloat(objectElement.getAttribute("x"));
        		float objectY = Float.parseFloat(objectElement.getAttribute("y"));
        		float objectWidth = Float.parseFloat(objectElement.getAttribute("width"));
        		float objectHeight = Float.parseFloat(objectElement.getAttribute("height"));
        		objectY = mapPixelHeight - objectY - objectHeight;
                
                if(objectElement.getAttribute("type").equals("Warp"))
                {
                	float destinationX = 0;
                	float destinationY = 0;
                	float hitboxXOffset = 0;
                	float hitboxYOffset = 0;
                	float hitboxWidth = 0;
                	float hitboxHeight = 0;
                	String destination = "world1-1";
                	String triggerKey = "DOWN";
                
            		keyMap.put("DOWN", Keys.DOWN);
            		keyMap.put("UP", Keys.UP);
            		keyMap.put("RIGHT", Keys.RIGHT);
            		keyMap.put("LEFT", Keys.LEFT);
                	
                	NodeList propertyList = objectElement.getElementsByTagName("property");
                	
                	for(int j = 0; j < propertyList.getLength(); j++)
                	{
                		Node propertyNode = propertyList.item(j);
                		if(objectNode.getNodeType() == Node.ELEMENT_NODE)
                		{
                 			Element propertyElement = (Element)propertyNode;
                    		String attribute = propertyElement.getAttribute("name");
                    		
                    		if(attribute.equals("Destination"))
                    		{
                    			destination = propertyElement.getAttribute("value");
                    		}
                    		else if(attribute.equals("Destination X"))
                    		{
                    			destinationX = Float.parseFloat(propertyElement.getAttribute("value"));
                    		}
                    		else if(attribute.equals("Destination Y"))
                    		{
                    			destinationY = Float.parseFloat(propertyElement.getAttribute("value"));
                    		}
                    		else if(attribute.equals("Hitbox X Offset"))
                    		{
                    			hitboxXOffset = Float.parseFloat(propertyElement.getAttribute("value"));
                    		}
                    		else if(attribute.equals("Hitbox Y Offset"))
                    		{
                    			hitboxYOffset = Float.parseFloat(propertyElement.getAttribute("value"));
                    		}
                    		else if(attribute.equals("Hitbox Width"))
                    		{
                    			hitboxWidth = Float.parseFloat(propertyElement.getAttribute("value"));
                    		}
                    		else if(attribute.equals("Hitbox Height"))
                    		{
                    			hitboxHeight = Float.parseFloat(propertyElement.getAttribute("value"));
                    		}
                    		else if(attribute.equals("Trigger Key"))
                    		{
                    			triggerKey = propertyElement.getAttribute("value");;
                    		}
                		}
                	}
                	
                	object.add(new TypeComponent(TypeComponent.Type.EVENT));
                	object.add(new PositionComponent(objectX, objectY));
            		object.add(new HitboxComponent(objectX + hitboxXOffset, objectY + hitboxYOffset, hitboxWidth, hitboxHeight));
            		object.add(new PhaseComponent());
            		
            		Array<Object> args = new Array<>();
            		args.add(object);
					args.add(game);
					args.add(keyMap.get(triggerKey));
					args.add(destination);
					args.add(destinationX);
					args.add(destinationY);
					
					object.add(new ScriptComponent("scripts\\warp.lua", args));
                }
                else if(objectElement.getAttribute("type").equals("Deadzone"))
                {
                	object.add(new TypeComponent(TypeComponent.Type.EVENT));
                	object.add(new PositionComponent(objectX, objectY));
            		object.add(new HitboxComponent(objectX, objectY, objectWidth, 1));
            		object.add(new PhaseComponent());
            		
            		Array<Object> args = new Array<>();
            		args.add(object);
					args.add(game);
					
					object.add(new ScriptComponent("scripts\\deadzone.lua", args));
                }
                else if(objectElement.getAttribute("type").equals("Goal"))
                {
                	object.add(new TypeComponent(TypeComponent.Type.EVENT));
                	object.add(new PositionComponent(objectX, objectY));
            		object.add(new HitboxComponent(objectX+7, objectY, 2, objectHeight));
            		object.add(new PhaseComponent());
            		
            		Array<Object> args = new Array<>();
            		args.add(object);
					args.add(game);
					
					object.add(new ScriptComponent("scripts\\goal.lua", args));
                }
                else if(objectElement.getAttribute("type").equals("Collision"))
                {
                	object.add(new TypeComponent(TypeComponent.Type.OBSTACLE));
                    object.add(new HitboxComponent(objectX, objectY, objectWidth, objectHeight));
                }
        		
                entities.add(object);
            }
        }
        
        return entities;
	}
}
