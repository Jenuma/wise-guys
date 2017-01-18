package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.StateComponent;
import io.whitegoldlabs.wiseguys.component.VelocityComponent;
import io.whitegoldlabs.wiseguys.util.Mappers;

public class PlayerInputSystem extends EntitySystem
{
	private Entity player;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public PlayerInputSystem(Entity player)
	{
		this.player = player;
	}
	
	public void update(float deltaTime)
	{
		VelocityComponent playerVelocity = Mappers.velocity.get(player);
		AccelerationComponent playerAcceleration = Mappers.acceleration.get(player);
		StateComponent playerState = Mappers.movingState.get(player);
		
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
        	playerState.airborneState = StateComponent.AirborneState.JUMPING;
        	playerVelocity.y += 430;
        }
	}
}
