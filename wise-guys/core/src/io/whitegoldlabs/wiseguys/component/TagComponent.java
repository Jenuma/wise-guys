package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class TagComponent implements Component
{
	public String tag;
	
	public TagComponent(String tag)
	{
		this.tag = tag;
	}
}
