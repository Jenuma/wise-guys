package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
import io.whitegoldlabs.wiseguys.component.AirborneStateComponent;
import io.whitegoldlabs.wiseguys.component.FacingDirectionStateComponent;
import io.whitegoldlabs.wiseguys.component.MovingStateComponent;
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
		FacingDirectionStateComponent playerFacingState = Mappers.facingState.get(player);
		AirborneStateComponent playerAirborneState = Mappers.airborneState.get(player);
		MovingStateComponent playerMovingState = Mappers.movingState.get(player);
		
		boolean leftPressed = Gdx.input.isKeyPressed(Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Keys.RIGHT);
        
        // Stop moving.
        if(leftPressed == rightPressed)
        {
        	if(playerMovingState.currentState == MovingStateComponent.State.MOVING_LEFT)
        	{
        		playerMovingState.currentState = MovingStateComponent.State.SLOWING_LEFT;
        	}
        	else if(playerMovingState.currentState == MovingStateComponent.State.MOVING_RIGHT)
        	{
        		playerMovingState.currentState = MovingStateComponent.State.SLOWING_RIGHT;
        	}
        }
        // Move left.
        else if(leftPressed)
		{
        	playerFacingState.currentState = FacingDirectionStateComponent.State.FACING_LEFT;
        	playerAcceleration.x = -15;
        	playerMovingState.currentState = MovingStateComponent.State.MOVING_LEFT;
		}
        //Move right.
        else if(rightPressed)
        {
        	playerFacingState.currentState = FacingDirectionStateComponent.State.FACING_RIGHT;
        	playerAcceleration.x = 15;
        	playerMovingState.currentState = MovingStateComponent.State.MOVING_RIGHT;
        }
        
        // Jump
        if(Gdx.input.isKeyJustPressed(Keys.Z) && playerAirborneState.currentState == AirborneStateComponent.State.ON_GROUND)
        {
        	playerAirborneState.currentState = AirborneStateComponent.State.JUMPING;
        	playerVelocity.y += 430;
        }
	}
}
