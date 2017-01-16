package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class FacingDirectionStateComponent implements Component
{
	public enum State
	{
		FACING_LEFT,
		FACING_RIGHT
	}
	
	public State currentState;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public FacingDirectionStateComponent(State state)
	{
		this.currentState = state;
	}
}
