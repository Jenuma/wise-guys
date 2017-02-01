package io.whitegoldlabs.wiseguys.util;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class ScriptManager
{
	// ---------------------------------------------------------------------------------|
	// Fields                                                                           |
	// ---------------------------------------------------------------------------------|
	private Globals globals;
	private Array<String> loadedScripts;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public ScriptManager()
	{
		this.globals = JsePlatform.standardGlobals();
		this.loadedScripts = new Array<>();
	}
	
	// ---------------------------------------------------------------------------------|
	// Public Methods                                                                   |
	// ---------------------------------------------------------------------------------|
	public void loadScript(String scriptName)
	{
		if(!loadedScripts.contains(scriptName, false))
		{
			globals.loadfile(scriptName).call();
			loadedScripts.add(scriptName);
		}
	}
	
	public Array<String> getLoadedScripts()
	{
		return this.loadedScripts;
	}
	
	public void executeScript(String moduleName, LuaValue[] args)
	{
		Gdx.app.log("[SCRIPTS]", "Executing script: " + moduleName);
		globals.get(moduleName).get("execute").invoke(args);
	}
}
