package io.whitegoldlabs.wiseguys.view;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.AnimationComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.InventoryComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.TypeComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.model.World;
import io.whitegoldlabs.wiseguys.system.AnimationSystem;
import io.whitegoldlabs.wiseguys.system.CollisionSystem;
import io.whitegoldlabs.wiseguys.system.DebugRenderSystem;
import io.whitegoldlabs.wiseguys.system.GravitySystem;
import io.whitegoldlabs.wiseguys.system.MovementSystem;
import io.whitegoldlabs.wiseguys.system.PickupSystem;
import io.whitegoldlabs.wiseguys.system.PlayerInputSystem;
import io.whitegoldlabs.wiseguys.system.RenderSystem;
import io.whitegoldlabs.wiseguys.util.Assets;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class GameScreen implements Screen
{
	final WiseGuys game;
	
	World world;
	int subworld;
	Array<Entity> updatedEntities;
	
	SpriteBatch hudBatch;	
	Texture spriteSheet;
	Sprite coinSprite;
	
	OrthographicCamera camera;
	
	Engine engine;
	DebugRenderSystem debugRenderSystem;
	
	Entity player;
	PositionComponent playerPosition;
	VelocityComponent playerVelocity;
    InventoryComponent playerInventory;
	
	boolean debugMode = false;
	
	short time = 400;
	float timer = 0;
	
	// ---------------------------------------------------------------------------------|
	// Constructors                                                                     |
	// ---------------------------------------------------------------------------------|
	public GameScreen(final WiseGuys game, String worldFile, float x, float y)
	{
		this.game = game;
		
		initHud();
		initCamera();
		initPlayer(x, y);
		initEngine(worldFile);
	}
	
	public GameScreen(final WiseGuys game, Entity player, String worldFile, float x, float y)
	{
		this.game = game;
		
		initHud();
		initCamera();
		
		this.player = player;

		Mappers.position.get(player).x = x;
		Mappers.position.get(player).y = y;
		
		initEngine(worldFile);
	}
	
	public GameScreen(final WiseGuys game, Entity player, Engine engine, World world, int subworld, float x, float y)
	{
		this.game = game;
		
		initHud();
		initCamera();
		
		this.player = player;
		
		Mappers.position.get(player).x = x;
		Mappers.position.get(player).y = y;
		
		this.engine = engine;
		engine.removeAllEntities();
		
		this.world = world;
		this.subworld = subworld;
		
		for(Entity entity : world.getWorldEntities().get(subworld))
		{
			engine.addEntity(entity);
		}
		
		engine.addEntity(player);
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
        
        if(Gdx.input.isKeyJustPressed(Keys.NUM_2))
        {
        	engine.removeEntity(player);
        	game.setScreen(new GameScreen(game, player, engine, world, 0, 16, 32));
        }
        
        if(Gdx.input.isKeyJustPressed(Keys.NUM_3))
        {
        	engine.removeEntity(player);
        	game.setScreen(new GameScreen(game, player, engine, world, 1, 16, 32));
        }
        
        engine.update(delta);

        updatedEntities = new Array<Entity>();
        for(Entity entity : engine.getEntities())
        {
        	if(entity != player)
        	{
        		updatedEntities.add(entity);
        	}
        }
        
        world.getWorldEntities().set(subworld, updatedEntities);
        //updatedEntities.clear();
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
	
	
	private void initHud()
	{
		hudBatch = new SpriteBatch();
		
		spriteSheet = Assets.spriteSheet;
		
		coinSprite = new Sprite(spriteSheet, 96, 0, 16, 16);
		coinSprite.setPosition(400, 662);
		coinSprite.setScale(2);
	}
	
	private void initCamera()
	{
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom -= 0.7f;
	}
	
	private void initPlayer(float x, float y)
	{
		Sprite playerStillSprite = new Sprite(spriteSheet, 0, 0, 16, 16);
		Array<Sprite> playerStillSprites = new Array<>(); 
		Array<Sprite> playerJumpingSprites = new Array<>();
		Array<Sprite> playerMovingSprites = new Array<>();
		Array<Sprite> playerSlowingSprites = new Array<>();
		
		playerStillSprites.add(playerStillSprite);
		playerJumpingSprites.add(new Sprite(spriteSheet, 64, 0, 16, 16));
		playerMovingSprites.add(new Sprite(spriteSheet, 16, 0, 16, 16));
		playerMovingSprites.add(new Sprite(spriteSheet, 32, 0, 16, 16));
		playerMovingSprites.add(new Sprite(spriteSheet, 48, 0, 16, 16));
		playerSlowingSprites.add(playerStillSprite);
		
		Sprite playerHitboxSprite = new Sprite(spriteSheet, 144, 144, 16, 16);
		
		player = new Entity();
		player.add(new TypeComponent(TypeComponent.Type.PLAYER));
		player.add(new SpriteComponent(playerStillSprite));
		
		StateComponent state = new StateComponent();
		state.motionState = StateComponent.MotionState.STILL;
		state.directionState = StateComponent.DirectionState.RIGHT;
		state.airborneState = StateComponent.AirborneState.GROUNDED;
		player.add(state);
		
		player.add(new PositionComponent(x, y));
		player.add(new VelocityComponent(0, 0));
		player.add(new AccelerationComponent(0, 0));
		player.add(new InventoryComponent(0, (byte)0, (byte)3));
		player.add(new HitboxComponent
		(
			x,
			y,
			playerStillSprite.getWidth(),
			playerStillSprite.getHeight(),
			playerHitboxSprite
		));
		
		AnimationComponent animation = new AnimationComponent();
		animation.animations.put("STILL", new Animation<Sprite>(1f/32f, playerStillSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("MOVING", new Animation<Sprite>(1f/32f, playerMovingSprites, Animation.PlayMode.LOOP));
		animation.animations.put("SLOWING", new Animation<Sprite>(1f/32f, playerSlowingSprites, Animation.PlayMode.NORMAL));
		animation.animations.put("JUMPING", new Animation<Sprite>(1f/32f, playerJumpingSprites, Animation.PlayMode.NORMAL));
		player.add(animation);
	}
	
	private void initEngine(String worldName)
	{
		engine = new Engine();
		engine.addSystem(new MovementSystem());
		engine.addSystem(new GravitySystem());
		engine.addSystem(new CollisionSystem());
		engine.addSystem(new RenderSystem(game.batch, camera));
		engine.addSystem(new PickupSystem(game, this, engine, player));
		engine.addSystem(new PlayerInputSystem(player));
		
		engine.addSystem(new AnimationSystem());
		
		SpriteBatch debugBatch = new SpriteBatch();
		debugRenderSystem = new DebugRenderSystem(game.batch, debugBatch, camera, game.font, player);
		engine.addSystem(debugRenderSystem);
		
		world = new World(worldName);
		
		for(Entity worldObject : world.getWorldEntities().get(0))
		{
			engine.addEntity(worldObject);
		}
		
		engine.addEntity(player);
		
		debugRenderSystem.setProcessing(debugMode);
	}
}
