package io.whitegoldlabs.wiseguys.component;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.badlogic.ashley.core.Component;

public class ScriptComponent implements Component
{
	public String scriptName;
	public LuaValue[] arguments;
	
	public ScriptComponent(String scriptName, Object[] arguments)
	{
		this.scriptName = scriptName;
		this.arguments = new LuaValue[arguments.length];
		
		for(int i = 0; i < arguments.length; i++)
		{
			this.arguments[i] = CoerceJavaToLua.coerce(arguments[i]);
		}
	}
}
