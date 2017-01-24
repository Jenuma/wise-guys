package io.whitegoldlabs.wiseguys.view;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.InventoryComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.system.AnimationSystem;
import io.whitegoldlabs.wiseguys.system.CollisionSystem;
import io.whitegoldlabs.wiseguys.system.DebugRenderSystem;
import io.whitegoldlabs.wiseguys.system.GravitySystem;
import io.whitegoldlabs.wiseguys.system.MovementSystem;
import io.whitegoldlabs.wiseguys.system.PlayerInputSystem;
import io.whitegoldlabs.wiseguys.system.ReaperSystem;
import io.whitegoldlabs.wiseguys.system.RenderSystem;
import io.whitegoldlabs.wiseguys.system.ScriptSystem;
import io.whitegoldlabs.wiseguys.util.Assets;
import io.whitegoldlabs.wiseguys.util.Mappers;
import io.whitegoldlabs.wiseguys.util.Worlds;

import static io.whitegoldlabs.wiseguys.component.StateComponent.AirborneState;
import static io.whitegoldlabs.wiseguys.component.StateComponent.DirectionState;
import static io.whitegoldlabs.wiseguys.component.StateComponent.MotionState;

public class GameScreen implements Screen
{
	final WiseGuys game;
	
	SpriteBatch hudBatch;	
	Texture spriteSheet;
	Sprite coinSprite;
	
	String worldName;
	
	DebugRenderSystem debugRenderSystem;
	
	PositionComponent playerPosition;
	VelocityComponent playerVelocity;
    InventoryComponent playerInventory;
    
    ScriptComponent julesDeathScript;
	
	boolean debugMode = false;
	float timer = 0;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public GameScreen(final WiseGuys game, String worldName, float x, float y)
	{
		this.game = game;
		this.worldName = worldName.substring(5, 8);
		
		initHud();
		
		Mappers.position.get(game.player).x = x;
		Mappers.position.get(game.player).y = y;
		Mappers.velocity.get(game.player).x = 0;
		Mappers.velocity.get(game.player).y = 0;
		Mappers.acceleration.get(game.player).x = 0;
		Mappers.acceleration.get(game.player).y = 0;
		Mappers.hitbox.get(game.player).hitbox.x = x;
		Mappers.hitbox.get(game.player).hitbox.y = y;
		
		StateComponent playerState = Mappers.state.get(game.player);
		playerState.airborneState = AirborneState.GROUNDED;
		playerState.directionState = DirectionState.RIGHT;
		playerState.motionState = MotionState.STILL;
		
		initEngine(worldName);
		
		Array<Object> args = new Array<>();
		args.add(game);
		args.add(Gdx.audio.newSound(Gdx.files.internal("jules_death.wav")));
		this.julesDeathScript = new ScriptComponent(false, "jules_death.lua", args);
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
        
		// Resetting the delta here prevents the engine from updating based on longer deltas that resulted from a thread pause.
		if(game.wasSleeping)
		{
			delta = 0;
			game.wasSleeping = false;
		}
		
		game.scriptManager.executeScriptIfReady();
		
		playerPosition = Mappers.position.get(game.player);
		playerVelocity = Mappers.velocity.get(game.player);
	    playerInventory = Mappers.inventory.get(game.player);
	    
	    // Camera Following
	    if(playerPosition.x <= 208)
        {
        	game.camera.position.set(208, 108, 0);
        }
        else
        {
        	game.camera.position.set(playerPosition.x, 108, 0);
        }
	    
	    game.engine.update(delta);
	    
	    // HUD
        hudBatch.begin();

        game.bigFont.draw(hudBatch, "JULES", 5, 710);
        game.bigFont.draw(hudBatch, String.format("%06d", playerInventory.score), 5, 680);
        
        coinSprite.draw(hudBatch);
        game.font.draw(hudBatch, "X", 425, 675);
        game.bigFont.draw(hudBatch, String.format("%02d", playerInventory.coins), 448, 680);
        
        game.bigFont.draw(hudBatch, "WORLD", 745, 710);
        game.bigFont.draw(hudBatch, worldName, 777, 680);
        
        game.bigFont.draw(hudBatch, "TIME", 1138, 710);
        game.bigFont.draw(hudBatch, String.format("%03d", playerInventory.time), 1170, 680);

        hudBatch.end();
        
        // Timer
        timer += delta;
        if(timer > 1)
        {
        	playerInventory.time--;
        	timer = 0;
        }
        
        // Player dies if time falls below 0.
        if(playerInventory.time < 0)
        {
        	game.scriptManager.executeScriptImmediately(julesDeathScript);
        }
        
        // Debug Mode
        if(Gdx.input.isKeyJustPressed(Keys.F4))
        {
        	debugMode = !debugMode;
        	debugRenderSystem.setProcessing(debugMode);
        }
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
	
	// ---------------------------------------------------------------------------------|
	// Private Methods                                                                  |
	// ---------------------------------------------------------------------------------|
	private void initHud()
	{
		hudBatch = new SpriteBatch();
		
		spriteSheet = Assets.spriteSheet;
		
		coinSprite = new Sprite(spriteSheet, 96, 0, 16, 16);
		coinSprite.setPosition(400, 662);
		coinSprite.setScale(2);
	}
	
	private void initEngine(String worldName)
	{
		game.engine.removeAllEntities();
		
		for(Entity worldObject : Worlds.getWorld(game, worldName))
		{
			game.engine.addEntity(worldObject);
		}
		
		loadScripts();
		
		game.engine.addSystem(new ReaperSystem(game.engine));
		game.engine.addSystem(new MovementSystem());
		game.engine.addSystem(new GravitySystem());
		game.engine.addSystem(new CollisionSystem());
		game.engine.addSystem(new ScriptSystem(game.player, game.scriptManager));
		game.engine.addSystem(new RenderSystem(game.batch, game.camera));
		game.engine.addSystem(new PlayerInputSystem(game.player));
		game.engine.addSystem(new AnimationSystem());
		
		SpriteBatch debugBatch = new SpriteBatch();
		debugRenderSystem = new DebugRenderSystem(game, debugBatch, game.camera);
		game.engine.addSystem(debugRenderSystem);
		
		game.engine.addEntity(game.player);
		
		debugRenderSystem.setProcessing(debugMode);
	}
	
	private void loadScripts()
	{
		ImmutableArray<Entity> scriptedEntities = game.engine.getEntitiesFor(Family.all
		(
			ScriptComponent.class
		).get());
		
		for(Entity scriptedEntity : scriptedEntities)
		{
			game.scriptManager.loadScript(Mappers.script.get(scriptedEntity).scriptName);
		}
		
		game.scriptManager.loadScript("jules_death.lua");
	}
}
