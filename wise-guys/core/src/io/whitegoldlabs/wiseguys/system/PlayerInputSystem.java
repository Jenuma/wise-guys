package io.whitegoldlabs.wiseguys.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import io.whitegoldlabs.wiseguys.component.AccelerationComponent;
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
		MovingStateComponent playerMovingState = Mappers.movingState.get(player);
		
		boolean leftPressed = Gdx.input.isKeyPressed(Keys.LEFT);
        boolean rightPressed = Gdx.input.isKeyPressed(Keys.RIGHT);
        
        // Stop moving.
        if(leftPressed == rightPressed)
        {
        	if(playerMovingState.motionState == MovingStateComponent.MotionState.MOVING)
        	{
        		playerMovingState.motionState = MovingStateComponent.MotionState.SLOWING;
        	}
        }
        // Move left.
        else if(leftPressed)
		{
        	playerMovingState.directionState = MovingStateComponent.DirectionState.LEFT;
        	playerAcceleration.x = -15;
        	playerMovingState.motionState = MovingStateComponent.MotionState.MOVING;
		}
        //Move right.
        else if(rightPressed)
        {
        	playerMovingState.directionState = MovingStateComponent.DirectionState.RIGHT;
        	playerAcceleration.x = 15;
        	playerMovingState.motionState = MovingStateComponent.MotionState.MOVING;
        }
        
        // Jump
        if(Gdx.input.isKeyJustPressed(Keys.Z) && playerMovingState.airborneState == MovingStateComponent.AirborneState.GROUNDED)
        {
        	playerMovingState.airborneState = MovingStateComponent.AirborneState.JUMPING;
        	playerVelocity.y += 430;
        }
	}
}
