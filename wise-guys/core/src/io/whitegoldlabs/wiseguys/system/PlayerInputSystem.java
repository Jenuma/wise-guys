package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.PlayerComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
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
		}
        
        // Pause
        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
        {
        	game.assets.manager.get(Assets.sfxPause).play();
        	game.isRunning = !game.isRunning;
        }
	}
}
