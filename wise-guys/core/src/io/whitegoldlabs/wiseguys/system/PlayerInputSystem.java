package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.BehaviorComponent;
import io.whitegoldlabs.wiseguys.component.HitboxComponent;
import io.whitegoldlabs.wiseguys.component.PhaseComponent;
import io.whitegoldlabs.wiseguys.component.PlayerComponent;
import io.whitegoldlabs.wiseguys.component.PositionComponent;
import io.whitegoldlabs.wiseguys.component.ScriptComponent;
import io.whitegoldlabs.wiseguys.component.SpriteComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.TypeComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Assets;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class PlayerInputSystem extends EntitySystem
{
	private final WiseGuys game;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public PlayerInputSystem(final WiseGuys game)
	{
		this.game = game;
		
		game.assets.manager.load(Assets.sfxJump);
		game.assets.manager.load(Assets.sfxPause);
		game.assets.manager.finishLoading();
	}
	
	@Override
	public void update(float deltaTime)
	{
		if(game.isRunning && !game.eventProcessing)
		{
			VelocityComponent playerVelocity = Mappers.velocity.get(game.player);
			AccelerationComponent playerAcceleration = Mappers.acceleration.get(game.player);
			StateComponent playerState = Mappers.state.get(game.player);
			
			boolean leftPressed = Gdx.input.isKeyPressed(Keys.LEFT);
	        boolean rightPressed = Gdx.input.isKeyPressed(Keys.RIGHT);
	        
	        // Stop moving.
	        if(leftPressed == rightPressed)
	        {
	        	if(playerState.motionState == StateComponent.MotionState.MOVING)
	        	{
	        		playerState.motionState = StateComponent.MotionState.SLOWING;
	        	}
	        }
	        // Move left.
	        else if(leftPressed)
			{
	        	playerState.directionState = StateComponent.DirectionState.LEFT;
	        	playerAcceleration.x = -0.2f;
	        	playerState.motionState = StateComponent.MotionState.MOVING;
			}
	        //Move right.
	        else if(rightPressed)
	        {
	        	playerState.directionState = StateComponent.DirectionState.RIGHT;
	        	playerAcceleration.x = 0.2f;
	        	playerState.motionState = StateComponent.MotionState.MOVING;
	        }
	        
	        // Jump
	        if(Gdx.input.isKeyJustPressed(Keys.Z) && playerState.airborneState == StateComponent.AirborneState.GROUNDED)
	        {
	        	game.assets.manager.get(Assets.sfxJump).play();
	        	playerState.airborneState = StateComponent.AirborneState.JUMPING;
	        	
	        	if(Mappers.player.get(game.player).playerState != PlayerComponent.PlayerState.NORMAL)
	        	{
	        		playerVelocity.y += 5.5;
	        	}
	        	else
	        	{
	        		playerVelocity.y += 4.9;
	        	}
	        }
	        
	        // Ascent Control
	        if(Gdx.input.isKeyPressed(Keys.Z) && playerState.airborneState == StateComponent.AirborneState.JUMPING && playerVelocity.y > 0)
	        {
	        	playerAcceleration.y += 0.4;
	        }
	        
	        // Player Projectile
	        if(Gdx.input.isKeyJustPressed(Keys.X))
	        {
	        	game.assets.manager.get(Assets.sfxFireball).play();
	        	
	        	PositionComponent playerPosition = Mappers.position.get(game.player);
	        	
	        	Entity projectile = new Entity();
	        	projectile.add(new StateComponent());
	        	projectile.add(new TypeComponent(TypeComponent.Type.PLAYER_PROJECTILE));
	        	
	        	int positionXOffset = 0;
	        	if(playerState.directionState == StateComponent.DirectionState.RIGHT)
	        	{
	        		positionXOffset = 16;
	        	}
	        	
	        	PositionComponent projectilePosition = new PositionComponent(playerPosition.x + positionXOffset, playerPosition.y + 4);
	        	projectile.add(projectilePosition);
	        	projectile.add(new HitboxComponent(projectilePosition.x, projectilePosition.y, 8, 8));
	        	
	        	float xVelocity = 3.5f;
	        	if(playerState.directionState == StateComponent.DirectionState.LEFT)
	        	{
	        		xVelocity = -3.5f;
	        	}
	        	
	        	projectile.add(new VelocityComponent(xVelocity, -3.5f));
	        	projectile.add(new AccelerationComponent(0, 0));
	        	projectile.add(new PhaseComponent());
	        	projectile.add(new SpriteComponent(new Sprite(game.assets.manager.get(Assets.spriteSheet), 40, 128, 8, 8)));
	        	
	        	Array<Object> scriptArgs = new Array<>();
	        	scriptArgs.add(projectile);
	        	scriptArgs.add(game);
	        	
	        	ScriptComponent projectileScript = new ScriptComponent("scripts\\player_projectile.lua", scriptArgs);
	        	game.scriptManager.loadScript(projectileScript.scriptName);
	        	projectile.add(projectileScript);
	        	
	        	BehaviorComponent behaviorComponent = new BehaviorComponent("scripts\\player_projectile_behavior.lua", scriptArgs);
	        	game.scriptManager.loadScript(behaviorComponent.scriptName);
	        	projectile.add(behaviorComponent);
	        	
	        	game.engine.addEntity(projectile);
	        }
		}
        
        // Pause
        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
        {
        	game.assets.manager.get(Assets.sfxPause).play();
        	game.isRunning = !game.isRunning;
        }
	}
}
