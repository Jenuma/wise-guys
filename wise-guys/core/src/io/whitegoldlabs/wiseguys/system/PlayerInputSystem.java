package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;

import io.whitegoldlabs.wiseguys.WiseGuys;
import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class PlayerInputSystem extends EntitySystem
{
	private final WiseGuys game;
	
	private Sound sfxJump;
	private Sound sfxPause;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public PlayerInputSystem(final WiseGuys game)
	{
		this.game = game;
		
		this.sfxJump = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
		this.sfxPause = Gdx.audio.newSound(Gdx.files.internal("pause.wav"));
	}
	
	public void update(float deltaTime)
	{
		if(game.isRunning)
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
	        	playerAcceleration.x = -15;
	        	playerState.motionState = StateComponent.MotionState.MOVING;
			}
	        //Move right.
	        else if(rightPressed)
	        {
	        	playerState.directionState = StateComponent.DirectionState.RIGHT;
	        	playerAcceleration.x = 15;
	        	playerState.motionState = StateComponent.MotionState.MOVING;
	        }
	        
	        // Jump
	        if(Gdx.input.isKeyJustPressed(Keys.Z) && playerState.airborneState == StateComponent.AirborneState.GROUNDED)
	        {
	        	sfxJump.play();
	        	playerState.airborneState = StateComponent.AirborneState.JUMPING;
	        	playerVelocity.y += 430;
	        }
		}
        
        // Pause
        if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
        {
        	sfxPause.play();
        	game.isRunning = !game.isRunning;
        }
	}
}
