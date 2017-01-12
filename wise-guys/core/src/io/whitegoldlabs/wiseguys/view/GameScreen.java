package io.whitegoldlabs.wiseguys.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.system.MovementSystem;
import io.whitegoldlabs.wiseguys.system.RenderSystem;

public class GameScreen implements Screen
{
	final WiseGuys game;
	
	OrthographicCamera camera;
	
	Texture spriteSheet;
	
	Engine engine;
	ComponentMapper<PositionComponent> pMap;
	ComponentMapper<VelocityComponent> vMap;
	ComponentMapper<SpriteComponent> sMap;
	
	Entity player;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public GameScreen(final WiseGuys game)
	{
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom -= 0.5f;
		
		spriteSheet = new Texture(Gdx.files.internal("sprites.png"));
		Sprite playerSprite = new Sprite(spriteSheet, 0, 0, 16, 16);
		
		engine = new Engine();
		
		pMap = ComponentMapper.getFor(PositionComponent.class);
		vMap = ComponentMapper.getFor(VelocityComponent.class);
		sMap = ComponentMapper.getFor(SpriteComponent.class);

		engine.addSystem(new RenderSystem(game.batch, camera));
		engine.addSystem(new MovementSystem());
		
		player = new Entity();
		player.add(new SpriteComponent(playerSprite));
		player.add(new PositionComponent(400, 16));
		player.add(new VelocityComponent(0, 0));
		
		// Generate test world objects
		Array<Entity> testWorldObjects = getTestWorldObjects();
		
		engine.addEntity(player);
		for(Entity testWorldObject : testWorldObjects)
		{
			engine.addEntity(testWorldObject);
		}
	}

	@Override
	public void show()
	{

	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.4f, 0.6f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Update logic.
        PositionComponent playerPosition = pMap.get(player);
        VelocityComponent playerVelocity = vMap.get(player);
        
        camera.position.set(playerPosition.x, playerPosition.y + 50, 0);
        
        if(Gdx.input.isKeyPressed(Keys.LEFT))
		{
        	playerVelocity.x -= 50;
		}
        else if(Gdx.input.isKeyPressed(Keys.RIGHT))
        {
        	playerVelocity.x += 50;
        }
        else
        {
        	playerVelocity.x = 0;
        }
        
        engine.update(delta);
	}

	@Override
	public void resize(int width, int height)
	{

	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void hide()
	{

	}

	@Override
	public void dispose()
	{
		
	}
	
	private Array<Entity> getTestWorldObjects()
	{
		Array<Entity> entities = new Array<>();
		
		String[] lines = Gdx.files.internal("testworld.txt").readString().split("\n");
		Pattern regex = Pattern.compile("(\\D+)\\s(\\d+)\\s(\\d+)");
		
		for(String line : lines)
		{
			Matcher matcher = regex.matcher(line);
			
			if(matcher.find())
			{
				int x = Integer.parseInt(matcher.group(2));
				int y = Integer.parseInt(matcher.group(3));
				
				Entity block = new Entity();
				Sprite blockSprite = new Sprite(spriteSheet, 80, 0, 16, 16);
				block.add(new SpriteComponent(blockSprite));
				block.add(new PositionComponent(x, y));
				
				entities.add(block);
			}
		}
		
		return entities;
	}
}
