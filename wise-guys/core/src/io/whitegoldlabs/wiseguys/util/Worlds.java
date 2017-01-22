package io.whitegoldlabs.wiseguys.util;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
	public static Array<Entity> getWorld(WiseGuys game, OrthographicCamera camera, String worldName)
	{
		if(!worldEntities.containsKey(worldName))
		{
			worldEntities.put(worldName, loadWorldEntitiesFromFile(game, camera, worldName));
		}
		
		return worldEntities.get(worldName);
	}
	
	// ---------------------------------------------------------------------------------|
	// loadWorldEntitiesFromFile                                                        |
	// ---------------------------------------------------------------------------------|
	private static Array<Entity> loadWorldEntitiesFromFile(WiseGuys game, OrthographicCamera camera, String worldName)
	{
		Array<Entity> entities = new Array<>();
		Entity entity;
		
		StateComponent state;
		Sprite hitboxSprite = null;
		
		boolean hasSprite = true;
		int spriteX = -1;
		int spriteY = -1;
		
		String[] csvLines = Gdx.files.internal(worldName + ".csv").readString().split("\r\n");
		
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
						
						Array<Object> args = new Array<>();
						args.add(Mappers.inventory.get(game.player));
						args.add(state);
						args.add(StateComponent.EnabledState.DISABLED);
						args.add(Gdx.audio.newSound(Gdx.files.internal("coin.wav")));
						
						entity.add(new ScriptComponent(false, "coin.lua", args));
					}
					// BOX
					else if(cells[x].equals("50"))
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
						
						Array<Sprite> boxStillSprites = new Array<>();
						boxStillSprites.add(new Sprite(Assets.spriteSheet, 0, 80, 16, 16));
						boxStillSprites.add(new Sprite(Assets.spriteSheet, 16, 80, 16, 16));
						boxStillSprites.add(new Sprite(Assets.spriteSheet, 32, 80, 16, 16));
						
						AnimationComponent ac = new AnimationComponent();
						ac.animations.put("STILL", new Animation<Sprite>(1f/4f, boxStillSprites, Animation.PlayMode.LOOP_PINGPONG));
						entity.add(ac);
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
					hitboxSprite = null;
				}
			}
		}
		
		Array<Entity> objects = loadWorldObjects(game, camera, worldName);
		
		for(Entity object : objects)
		{
			entities.add(object);
		}
		
		return entities;
	}
	
	private static Array<Entity> loadWorldObjects(WiseGuys game, OrthographicCamera camera, String worldName)
	{
		ArrayMap<String, Integer> keyMap = new ArrayMap<>();;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = null;
	    Document doc = null;
	     
		try
		{
			dBuilder = dbFactory.newDocumentBuilder();
		}
		catch(ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			doc = dBuilder.parse(worldName + ".tmx");
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	    doc.getDocumentElement().normalize();

	    Array<Entity> objectEntities = new Array<>();
	    Entity object;
	    
	    float mapHeight = Float.parseFloat(doc.getDocumentElement().getAttribute("height")) * 16;
	    
	    NodeList objectList = doc.getElementsByTagName("object");
        for(int i = 0; i < objectList.getLength(); i++)
        {
            Node objectNode = objectList.item(i);
            if(objectNode.getNodeType() == Node.ELEMENT_NODE)
            {
            	object = new Entity();
                Element objectElement = (Element)objectNode;
                
                float x = Float.parseFloat(objectElement.getAttribute("x"));
        		float y = Float.parseFloat(objectElement.getAttribute("y"));
        		float height = Float.parseFloat(objectElement.getAttribute("height"));
        		y = mapHeight - y - height;
                
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
                	
                	object.add(new PositionComponent(x, y));
            		object.add(new HitboxComponent
    				(
    					x + hitboxXOffset,
    					y + hitboxYOffset,
    					hitboxWidth,
    					hitboxHeight,
    					new Sprite(Assets.spriteSheet, 128, 144, 16, 16)
    				));
            		
            		Array<Object> args = new Array<>();
					args.add(game);
					args.add(Gdx.input);
					args.add(keyMap.get(triggerKey));
					args.add(Gdx.audio.newSound(Gdx.files.internal("pipe.wav")));
					args.add(camera);
					args.add(destination);
					args.add(destinationX);
					args.add(destinationY);
					
					object.add(new ScriptComponent(false, "warp.lua", args));
                }
                else if(objectElement.getAttribute("type").equals("Deadzone"))
                {
                	float width = Float.parseFloat(objectElement.getAttribute("width"));
                	
                	object.add(new PositionComponent(x, y));
            		object.add(new HitboxComponent
    				(
    					x,
    					y,
    					width,
    					1,
    					new Sprite(Assets.spriteSheet, 128, 144, 16, 16)
    				));
            		
            		Array<Object> args = new Array<>();
					args.add(game);
					args.add(Gdx.audio.newSound(Gdx.files.internal("jules_death.wav")));
					
					object.add(new ScriptComponent(false, "jules_death.lua", args));
                }
        		
                objectEntities.add(object);
                
                // Obstacle Hitboxes
//                float x = Float.parseFloat(eElement.getAttribute("x"));
//                float y = Float.parseFloat(eElement.getAttribute("y"));
//                float width = Float.parseFloat(eElement.getAttribute("width"));
//                float height = Float.parseFloat(eElement.getAttribute("height"));
//                
//                object.add(new HitboxComponent
//				(
//					x,
//					512 - y - height,
//					width,
//					height,
//					new Sprite(Assets.spriteSheet, 128, 144, 16, 16)
//				));
            }
        }
        
        return objectEntities;
	}
}
