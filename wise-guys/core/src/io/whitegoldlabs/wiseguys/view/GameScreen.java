package io.whitegoldlabs.wiseguys.view;

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

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.system.MovementSystem;

public class GameScreen implements Screen
{
	final WiseGuys game;
	
	OrthographicCamera camera;
	
	Texture spriteSheet;
	
	Engine engine;
	ComponentMapper<PositionComponent> pMap;
	ComponentMapper<VelocityComponent> vMap;
	ComponentMapper<SpriteComponent> sMap;
	MovementSystem movementSystem;
	Entity player;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public GameScreen(final WiseGuys game)
	{
		this.game = game;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		spriteSheet = new Texture(Gdx.files.internal("sprites.png"));
		
		engine = new Engine();
		
		pMap = ComponentMapper.getFor(PositionComponent.class);
		vMap = ComponentMapper.getFor(VelocityComponent.class);
		sMap = ComponentMapper.getFor(SpriteComponent.class);
		
		movementSystem = new MovementSystem();
		engine.addSystem(movementSystem);
		
		player = new Entity();
		player.add(new PositionComponent(400, 300));
		player.add(new VelocityComponent(0, 0));
		player.add(new SpriteComponent(new Sprite(spriteSheet, 0, 0, 16, 16)));
		engine.addEntity(player);
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
        
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        
        game.batch.begin();
        
        SpriteComponent playerSprite = sMap.get(player);
        PositionComponent playerPosition = pMap.get(player);
        playerSprite.sprite.setPosition(playerPosition.x, playerPosition.y);
        playerSprite.sprite.draw(game.batch);
        
        game.batch.end();
        
        // Update logic.
        VelocityComponent playerVelocity = vMap.get(player);
        
        if(Gdx.input.isKeyPressed(Keys.LEFT))
		{
        	playerVelocity.x = -5;
		}
        else if(Gdx.input.isKeyPressed(Keys.RIGHT))
        {
        	playerVelocity.x = 5;
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
}
