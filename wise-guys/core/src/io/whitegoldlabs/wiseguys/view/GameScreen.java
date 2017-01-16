package io.whitegoldlabs.wiseguys.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.system.CollisionSystem;
import io.whitegoldlabs.wiseguys.system.GravitySystem;
import io.whitegoldlabs.wiseguys.system.DebugRenderSystem;
import io.whitegoldlabs.wiseguys.system.MovementSystem;
import io.whitegoldlabs.wiseguys.system.RenderSystem;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class GameScreen implements Screen
{
	final WiseGuys game;

	SpriteBatch hudBatch;
	SpriteBatch debugBatch;
	
	Sprite coinSprite;
	
	OrthographicCamera camera;
	Texture spriteSheet;
	
	Engine engine;
	DebugRenderSystem hitboxRenderSystem;
	
	Entity player;
	
	boolean leftPressed;
	boolean rightPressed;
	
	boolean debugMode = false;
	
	final float PLAYER_SPAWN_X = 400;
	final float PLAYER_SPAWN_Y = 16;
	
	int score = 0;
	byte coins = 0;
	short time = 400;
	float timer = 0;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public GameScreen(final WiseGuys game)
	{
		this.game = game;
		
		hudBatch = new SpriteBatch();
		debugBatch = new SpriteBatch();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom -= 0.8f;
		
		spriteSheet = new Texture(Gdx.files.internal("sprites.png"));
		
		coinSprite = new Sprite(spriteSheet, 96, 0, 16, 16);
		coinSprite.setPosition(600, 1022);
		coinSprite.setScale(2);
		
		Sprite playerSprite = new Sprite(spriteSheet, 0, 0, 16, 16);
		Sprite playerHitboxSprite = new Sprite(spriteSheet, 144, 144, 16, 16);
		
		engine = new Engine();
		
		engine.addSystem(new MovementSystem());
		engine.addSystem(new GravitySystem());
		engine.addSystem(new CollisionSystem());
		
		engine.addSystem(new RenderSystem(game.batch, camera));
		
		hitboxRenderSystem = new DebugRenderSystem(game.batch, camera);
		engine.addSystem(hitboxRenderSystem);
		
		player = new Entity();
		player.add(new SpriteComponent(playerSprite));
		player.add(new StateComponent(StateComponent.State.ON_GROUND));
		player.add(new PositionComponent(PLAYER_SPAWN_X, PLAYER_SPAWN_Y));
		player.add(new VelocityComponent(0, 0));
		player.add(new AccelerationComponent(0, 0));
		player.add(new HitboxComponent
		(
			PLAYER_SPAWN_X,
			PLAYER_SPAWN_Y,
			playerSprite.getWidth(),
			playerSprite.getHeight(),
			HitboxComponent.Type.HITBOX,
			playerHitboxSprite)
		);
		
		// Generate test world objects.
		Array<Entity> testWorldObjects = getTestWorldObjects();
		
		engine.addEntity(player);
		for(Entity testWorldObject : testWorldObjects)
		{
			engine.addEntity(testWorldObject);
		}
		
		hitboxRenderSystem.setProcessing(debugMode);
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
        
        hudBatch.begin();
        game.bigFont.draw(hudBatch, "JULES", 5, 1070);
        game.bigFont.draw(hudBatch, String.format("%06d", score), 5, 1040);
        
        coinSprite.draw(hudBatch);
        game.font.draw(hudBatch, "X", 625, 1035);
        game.bigFont.draw(hudBatch, String.format("%02d", coins), 648, 1040);
        
        game.bigFont.draw(hudBatch, "WORLD", 1245, 1070);
        game.bigFont.draw(hudBatch, "0-0", 1277, 1040);
        
        game.bigFont.draw(hudBatch, "TIME", 1788, 1070);
        game.bigFont.draw(hudBatch, String.format("%03d", time), 1820, 1040);
        hudBatch.end();
        
        timer += delta;
        if(timer > 1)
        {
        	time--;
        	timer = 0;
        }
        
        // Update logic.
        PositionComponent playerPosition = Mappers.position.get(player);
        VelocityComponent playerVelocity = Mappers.velocity.get(player);
        StateComponent playerState = Mappers.state.get(player);
        
        if(Gdx.input.isKeyJustPressed(Keys.F4))
        {
        	debugMode = !debugMode;
        	hitboxRenderSystem.setProcessing(debugMode);
        }
        
        if(debugMode)
        {
        	debugBatch.begin();
            game.font.draw(debugBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, 80);
            game.font.draw(debugBatch, "Pos: " + playerPosition.x + "," + playerPosition.y, 5, 60);
            game.font.draw(debugBatch, "Vel: " + playerVelocity.x + "," + playerVelocity.y, 5, 40);
            game.font.draw(debugBatch, "Player State: " + Mappers.state.get(player).currentState, 5, 20);
            debugBatch.end();
        }
        
        camera.position.set(playerPosition.x, playerPosition.y + 50, 0);
        
        leftPressed = Gdx.input.isKeyPressed(Keys.LEFT);
        rightPressed = Gdx.input.isKeyPressed(Keys.RIGHT);
        
        // Move left or right.
        if(leftPressed == rightPressed)
        {
        	playerVelocity.x = 0;
        }
        else if(leftPressed)
		{
        	playerVelocity.x = -100;
		}
        else if(rightPressed)
        {
        	playerVelocity.x = 100;
        }
        
        // Jump
        if(Gdx.input.isKeyJustPressed(Keys.Z) && playerState.currentState == StateComponent.State.ON_GROUND)
        {
        	playerState.currentState = StateComponent.State.IN_AIR;
        	playerVelocity.y += 400;
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
				String tileType = matcher.group(1);
				int x = Integer.parseInt(matcher.group(2));
				int y = Integer.parseInt(matcher.group(3));
				
				if(tileType.equals("BLOCK"))
				{
					Entity block = new Entity();
					Sprite blockSprite = new Sprite(spriteSheet, 80, 0, 16, 16);
					Sprite blockHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					block.add(new SpriteComponent(blockSprite));
					block.add(new PositionComponent(x, y));
					block.add(new HitboxComponent
					(
						x,
						y,
						blockSprite.getWidth(),
						blockSprite.getHeight(),
						HitboxComponent.Type.HITBOX,
						blockHitboxSprite
					));
					
					entities.add(block);
				}
				else if(tileType.equals("COIN"))
				{
					Entity coin = new Entity();
					Sprite coinSprite = new Sprite(spriteSheet, 112, 0, 16, 16);
					Sprite coinCollectboxSprite = new Sprite(spriteSheet, 112, 144, 10, 16);
					coin.add(new SpriteComponent(coinSprite));
					coin.add(new PositionComponent(x, y));
					coin.add(new HitboxComponent
					(
						x+3,
						y,
						coinSprite.getWidth(),
						coinSprite.getHeight(),
						HitboxComponent.Type.COLLECTBOX,
						coinCollectboxSprite
					));
					
					entities.add(coin);
				}
			}
		}
		
		return entities;
	}
}
