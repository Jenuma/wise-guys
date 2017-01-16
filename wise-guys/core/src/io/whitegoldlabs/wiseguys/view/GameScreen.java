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
import io.whitegoldlabs.wiseguys.component.AirborneStateComponent;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.CollectboxComponent;
import io.whitegoldlabs.wiseguys.component.FacingDirectionStateComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.InventoryComponent;
import io.whitegoldlabs.wiseguys.component.MovingStateComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.system.AnimationSystem;
import io.whitegoldlabs.wiseguys.system.CollisionSystem;
import io.whitegoldlabs.wiseguys.system.DebugRenderSystem;
import io.whitegoldlabs.wiseguys.system.GravitySystem;
import io.whitegoldlabs.wiseguys.system.MovementSystem;
import io.whitegoldlabs.wiseguys.system.PickupSystem;
import io.whitegoldlabs.wiseguys.system.PlayerInputSystem;
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
	DebugRenderSystem debugRenderSystem;
	
	Entity player;
	PositionComponent playerPosition;
	VelocityComponent playerVelocity;
    AirborneStateComponent playerAirborneState;
    FacingDirectionStateComponent playerFacingState;
    InventoryComponent playerInventory;
	
	boolean leftPressed;
	boolean rightPressed;
	
	boolean debugMode = false;
	
	final float PLAYER_SPAWN_X = 16;
	final float PLAYER_SPAWN_Y = 32;
	
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
		camera.zoom -= 0.7f;
		
		spriteSheet = new Texture(Gdx.files.internal("sprites.png"));
		
		coinSprite = new Sprite(spriteSheet, 96, 0, 16, 16);
		coinSprite.setPosition(400, 662);
		coinSprite.setScale(2);
		
		Sprite playerStandingSprite = new Sprite(spriteSheet, 0, 0, 16, 16);
		Sprite playerJumpingSprite = new Sprite(spriteSheet, 64, 0, 16, 16);
		Array<Sprite> playerWalkingSprites = new Array<>();
		playerWalkingSprites.add(new Sprite(spriteSheet, 16, 0, 16, 16));
		playerWalkingSprites.add(new Sprite(spriteSheet, 32, 0, 16, 16));
		playerWalkingSprites.add(new Sprite(spriteSheet, 48, 0, 16, 16));
		
		Sprite playerHitboxSprite = new Sprite(spriteSheet, 144, 144, 16, 16);
		
		engine = new Engine();
		
		player = new Entity();
		player.add(new SpriteComponent(playerStandingSprite));
		player.add(new AirborneStateComponent(AirborneStateComponent.State.ON_GROUND));
		player.add(new FacingDirectionStateComponent(FacingDirectionStateComponent.State.FACING_RIGHT));
		player.add(new MovingStateComponent(MovingStateComponent.State.NOT_MOVING));
		player.add(new PositionComponent(PLAYER_SPAWN_X, PLAYER_SPAWN_Y));
		player.add(new VelocityComponent(0, 0));
		player.add(new AccelerationComponent(0, 0));
		player.add(new InventoryComponent(0, (byte)0, (byte)3));
		player.add(new AnimationComponent(playerStandingSprite, playerJumpingSprite, playerWalkingSprites));
		player.add(new HitboxComponent
		(
			PLAYER_SPAWN_X,
			PLAYER_SPAWN_Y,
			playerStandingSprite.getWidth(),
			playerStandingSprite.getHeight(),
			playerHitboxSprite
		));
		
		engine.addSystem(new MovementSystem());
		engine.addSystem(new GravitySystem());
		engine.addSystem(new CollisionSystem());
		engine.addSystem(new RenderSystem(game.batch, camera));
		engine.addSystem(new PickupSystem(engine, player));
		engine.addSystem(new PlayerInputSystem(player));
		engine.addSystem(new AnimationSystem());
		
		debugRenderSystem = new DebugRenderSystem(game.batch, debugBatch, camera, game.font, player);
		engine.addSystem(debugRenderSystem);
		
		// Generate test world objects.
		Array<Entity> testWorldObjects = getTestWorldObjects();
		
		engine.addEntity(player);
		for(Entity testWorldObject : testWorldObjects)
		{
			engine.addEntity(testWorldObject);
		}
		
		debugRenderSystem.setProcessing(debugMode);
	}

	@Override
	public void show()
	{

	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.42f, 0.55f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        playerPosition = Mappers.position.get(player);
		playerVelocity = Mappers.velocity.get(player);
	    playerAirborneState = Mappers.airborneState.get(player);
	    playerFacingState = Mappers.facingState.get(player);
	    playerInventory = Mappers.inventory.get(player);
        
	    // HUD
        hudBatch.begin();

        game.bigFont.draw(hudBatch, "JULES", 5, 710);
        game.bigFont.draw(hudBatch, String.format("%06d", playerInventory.score), 5, 680);
        
        coinSprite.draw(hudBatch);
        game.font.draw(hudBatch, "X", 425, 675);
        game.bigFont.draw(hudBatch, String.format("%02d", playerInventory.coins), 448, 680);
        
        game.bigFont.draw(hudBatch, "WORLD", 745, 710);
        game.bigFont.draw(hudBatch, "0-0", 777, 680);
        
        game.bigFont.draw(hudBatch, "TIME", 1138, 710);
        game.bigFont.draw(hudBatch, String.format("%03d", time), 1170, 680);

        hudBatch.end();
        
        // Timer
        timer += delta;
        if(timer > 1)
        {
        	time--;
        	timer = 0;
        }
        
        // Debug Mode
        if(Gdx.input.isKeyJustPressed(Keys.F4))
        {
        	debugMode = !debugMode;
        	debugRenderSystem.setProcessing(debugMode);
        }
        
        if(playerPosition.x <= 208)
        {
        	camera.position.set(208, 108, 0);
        }
        else
        {
        	camera.position.set(playerPosition.x, 108, 0);
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
		
		String[] lines = Gdx.files.internal("world1-1.txt").readString().split("\n");
		Pattern regex = Pattern.compile("(\\D+)\\s(\\d+)\\s(\\d+)");
		
		for(String line : lines)
		{
			Matcher matcher = regex.matcher(line);
			
			if(matcher.find())
			{
				String tileType = matcher.group(1);
				int x = Integer.parseInt(matcher.group(2));
				int y = Integer.parseInt(matcher.group(3));
				
				if(tileType.equals("GROUND"))
				{
					Entity ground = new Entity();
					Sprite groundSprite = new Sprite(spriteSheet, 16, 128, 16, 16);
					Sprite groundHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					ground.add(new SpriteComponent(groundSprite));
					ground.add(new PositionComponent(x, y));
					ground.add(new HitboxComponent
					(
						x,
						y,
						groundSprite.getWidth(),
						groundSprite.getHeight(),
						groundHitboxSprite
					));
					
					entities.add(ground);
				}
				else if(tileType.equals("BOX"))
				{
					Entity box = new Entity();
					Sprite boxSprite = new Sprite(spriteSheet, 32, 128, 16, 16);
					Sprite boxHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					box.add(new SpriteComponent(boxSprite));
					box.add(new PositionComponent(x, y));
					box.add(new HitboxComponent
					(
						x,
						y,
						boxSprite.getWidth(),
						boxSprite.getHeight(),
						boxHitboxSprite
					));
					
					entities.add(box);
				}
				else if(tileType.equals("BRICKS"))
				{
					Entity bricks = new Entity();
					Sprite bricksSprite = new Sprite(spriteSheet, 48, 128, 16, 16);
					Sprite bricksHitboxSprite = new Sprite(spriteSheet, 128, 144, 16, 16);
					bricks.add(new SpriteComponent(bricksSprite));
					bricks.add(new PositionComponent(x, y));
					bricks.add(new HitboxComponent
					(
						x,
						y,
						bricksSprite.getWidth(),
						bricksSprite.getHeight(),
						bricksHitboxSprite
					));
					
					entities.add(bricks);
				}
				else if(tileType.equals("BLOCK"))
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
					coin.add(new CollectboxComponent
					(
						x+3,
						y,
						coinCollectboxSprite.getWidth(),
						coinCollectboxSprite.getHeight(),
						CollectboxComponent.Type.COIN,
						coinCollectboxSprite
					));
					
					entities.add(coin);
				}
			}
		}
		
		return entities;
	}
}
