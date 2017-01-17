package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class MovingStateComponent implements Component
{
	public enum MotionState
	{
		STILL,
		MOVING,
		SLOWING,
		SPRINTING
	}
	
	public enum DirectionState
	{
		RIGHT,
		LEFT
	}
	
	public enum AirborneState
	{
		GROUNDED,
		JUMPING,
		FALLING
	}
	
	public MotionState motionState;
	public DirectionState directionState;
	public AirborneState airborneState;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public MovingStateComponent
	(MotionState motionState, DirectionState directionState, AirborneState airborneState)
	{
		this.motionState = motionState;
		this.directionState = directionState;
		this.airborneState = airborneState;
	}
}
