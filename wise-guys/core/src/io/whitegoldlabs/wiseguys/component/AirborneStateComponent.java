package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class AirborneStateComponent implements Component
{
	public enum State
	{
		ON_GROUND,
		FALLING,
		JUMPING
	}
	
	public State currentState;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public AirborneStateComponent(State state)
	{
		this.currentState = state;
	}
}
