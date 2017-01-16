package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class MovingStateComponent implements Component
{
	public enum State
	{
		NOT_MOVING,
		MOVING_RIGHT,
		MOVING_LEFT,
		SLOWING_RIGHT,
		SLOWING_LEFT
	}
	
	public State currentState;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public MovingStateComponent(State state)
	{
		this.currentState = state;
	}
}
