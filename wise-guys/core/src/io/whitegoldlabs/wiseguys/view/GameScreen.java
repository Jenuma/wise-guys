package io.whitegoldlabs.wiseguys.view;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.BehaviorComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PlayerComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent.AirborneState;
import io.whitegoldlabs.wiseguys.component.StateComponent.DirectionState;
import io.whitegoldlabs.wiseguys.component.StateComponent.MotionState;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.system.AnimationSystem;
import io.whitegoldlabs.wiseguys.system.BehaviorSystem;
import io.whitegoldlabs.wiseguys.system.CollisionSystem;
import io.whitegoldlabs.wiseguys.system.DebugRenderSystem;
import io.whitegoldlabs.wiseguys.system.GravitySystem;
import io.whitegoldlabs.wiseguys.system.MovementSystem;
import io.whitegoldlabs.wiseguys.system.PlayerInputSystem;
import io.whitegoldlabs.wiseguys.system.ReaperSystem;
import io.whitegoldlabs.wiseguys.system.RenderSystem;
import io.whitegoldlabs.wiseguys.util.Assets;
import io.whitegoldlabs.wiseguys.util.Mappers;
import io.whitegoldlabs.wiseguys.util.Worlds;

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
    PlayerComponent playerInventory;
    
    BehaviorComponent julesTimeOutBehavior;
    
    GlyphLayout pausedText;
	
	boolean debugMode = false;
	float timer = 0;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public GameScreen(final WiseGuys game, String worldName, float x, float y)
	{
		this.game = game;
		
		game.assets.manager.load(Assets.spriteSheet);
		game.assets.manager.load(Assets.sfxJulesDeath);
		game.assets.manager.finishLoading();
		
		this.worldName = worldName.substring(5, 8);
		
		initHud();
		initEngine(worldName);
		
		Mappers.position.get(game.player).x = x;
		Mappers.position.get(game.player).y = y;
		Mappers.velocity.get(game.player).x = 0;
		Mappers.velocity.get(game.player).y = 0;
		Mappers.acceleration.get(game.player).x = 0;
		Mappers.acceleration.get(game.player).y = 0;
		Mappers.hitbox.get(game.player).hitbox.x = x;
		Mappers.hitbox.get(game.player).hitbox.y = y;
		Mappers.sprite.get(game.player).sprite.setPosition(x, y);
		
		StateComponent playerState = Mappers.state.get(game.player);
		playerState.airborneState = AirborneState.GROUNDED;
		playerState.directionState = DirectionState.RIGHT;
		playerState.motionState = MotionState.STILL;
		
		Array<Object> args = new Array<>();
		args.add(game);
		args.add(game.assets.manager.get(Assets.sfxJulesDeath));
		this.julesTimeOutBehavior = new BehaviorComponent("scripts\\jules_death_behavior.lua", args);
		this.julesTimeOutBehavior.behaviorState = "DYING";
		
		pausedText = new GlyphLayout(game.bigFont, "PAUSED");
	}
	
	@Override
	public void show()
	{

	}
	
	@Override
	public void render(float delta)
	{
		if(game.nextGameScreenDestination != null)
		{
			game.setNextGameScreen();
		}
		
		Gdx.gl.glClearColor(0.42f, 0.55f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		playerPosition = Mappers.position.get(game.player);
		playerVelocity = Mappers.velocity.get(game.player);
	    playerInventory = Mappers.player.get(game.player);
	    
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

        if(!game.isRunning)
        {
        	game.bigFont.draw(hudBatch, pausedText,
    			(Gdx.graphics.getWidth() - pausedText.width) / 2,
    			(Gdx.graphics.getHeight() - pausedText.height) / 2);
        }
        hudBatch.end();
        
        if(game.isRunning && !game.eventProcessing)
        {
        	// Timer
            timer += delta;
            if(timer > 1)
            {
            	playerInventory.time--;
            	timer = 0;
            }
        }
        
        if(Mappers.player.get(game.player).damaged)
        {
        	Mappers.player.get(game.player).damagedTime += delta;
        	
        	if(Mappers.player.get(game.player).damagedTime > 2)
        	{
        		Mappers.player.get(game.player).damaged = false;
        		Mappers.player.get(game.player).damagedTime = 0;
        	}
        }
        
        // Player dies if time falls below 0.
        if(playerInventory.time < 0)
        {
        	game.player.add(julesTimeOutBehavior);
        }
        
        // Debug Mode
        if(Gdx.input.isKeyJustPressed(Keys.F4))
        {
        	debugMode = !debugMode;
        	debugRenderSystem.setProcessing(debugMode);
        }
        
        if(Gdx.input.justTouched())
        {
        	Vector3 mousePos = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        	
        	Rectangle mouseHitbox = new Rectangle(mousePos.x, mousePos.y, 1, 1);
        	
        	ImmutableArray<Entity> hitboxEntities = game.engine.getEntitiesFor(Family.all(HitboxComponent.class).get());
        	
        	for(Entity entity : hitboxEntities)
        	{
        		if(mouseHitbox.overlaps(Mappers.hitbox.get(entity).hitbox))
        		{
        			System.out.println("------------------------------------------------------------------------------------------");
        			System.out.println("ID: " + entity);
        			if(Mappers.type.has(entity)) {System.out.println("Type: " + Mappers.type.get(entity).type);}
        			if(Mappers.position.has(entity)) {System.out.println("Position: " + Mappers.position.get(entity).x + ", " + Mappers.position.get(entity).y);}
        			System.out.println("Hitbox: " + Mappers.hitbox.get(entity).hitbox.x + ", " + Mappers.hitbox.get(entity).hitbox.y + ", " + Mappers.hitbox.get(entity).hitbox.width + "x" + Mappers.hitbox.get(entity).hitbox.height);
        			if(Mappers.velocity.has(entity)) {System.out.println("Velocity: " + Mappers.velocity.get(entity).x + ", " + Mappers.velocity.get(entity).y);}
        			if(Mappers.acceleration.has(entity)) {System.out.println("Acceleration: " + Mappers.acceleration.get(entity).x + ", " + Mappers.acceleration.get(entity).y);}
        			if(Mappers.player.has(entity)) {System.out.println("Player State: " + Mappers.player.get(entity).playerState + ", " + Mappers.player.get(entity).anonState + ", " + (Mappers.player.get(entity).damaged ? "Damaged" : "Not Damaged"));}
        			if(Mappers.state.has(entity)) {System.out.println("State: " + Mappers.state.get(entity).motionState + ", " + Mappers.state.get(entity).airborneState + ", " + Mappers.state.get(entity).directionState + " for " + Mappers.state.get(entity).time + "s");}
        			if(Mappers.script.has(entity)) {System.out.println("Script: " + Mappers.script.get(entity).moduleName);}
        			if(Mappers.behavior.has(entity)) {System.out.println("Behavior: " + Mappers.behavior.get(entity).moduleName + ", currently " + Mappers.behavior.get(entity).behaviorState + " for " + Mappers.behavior.get(entity).behaviorTime + "s");}
        			System.out.println("------------------------------------------------------------------------------------------");
        		}
        	}
        }
        
    	game.console.draw();
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
		
		spriteSheet = game.assets.manager.get(Assets.spriteSheet);
		
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
		
		game.engine.addSystem(new ReaperSystem(game));
		game.engine.addSystem(new MovementSystem(game));
		game.engine.addSystem(new GravitySystem(game));
		game.engine.addSystem(new CollisionSystem(game));
		game.engine.addSystem(new BehaviorSystem(game));
		game.engine.addSystem(new RenderSystem(game));
		game.engine.addSystem(new PlayerInputSystem(game));
		game.engine.addSystem(new AnimationSystem(game));
		
		SpriteBatch debugBatch = new SpriteBatch();
		debugRenderSystem = new DebugRenderSystem(game, debugBatch);
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
		
		game.scriptManager.loadScript("scripts\\jules_death_behavior.lua");
	}
}
