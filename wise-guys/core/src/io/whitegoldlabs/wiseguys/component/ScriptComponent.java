package io.whitegoldlabs.wiseguys.component;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.badlogic.ashley.core.Component;

public class ScriptComponent implements Component
{
	// ---------------------------------------------------------------------------------|
	// Fields                                                                           |
	// ---------------------------------------------------------------------------------|
	public String scriptName;
	public String moduleName;
	public LuaValue[] args;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public ScriptComponent(String scriptName, Object[] args)
	{
		this.scriptName = scriptName;
		this.moduleName = scriptName.substring(0, 1).toUpperCase() +
			scriptName.substring(1, scriptName.indexOf('.'));
		
		this.args = new LuaValue[args.length];
		
		for(int i = 0; i < args.length; i++)
		{
			this.args[i] = CoerceJavaToLua.coerce(args[i]);
		}
	}
}
