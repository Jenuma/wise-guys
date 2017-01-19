package io.whitegoldlabs.wiseguys.util;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class ScriptManager
{
	private Globals globals = JsePlatform.standardGlobals();
	
	public ScriptManager(String scriptName)
	{
		globals.loadfile(scriptName).call();
	}
	
	public void execute(LuaValue[] arguments)
	{
		globals.get("execute").invoke(arguments);
	}
}
