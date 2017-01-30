Goal = {}

function Goal.execute(goal, game)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(goal)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	---------------------------
  -- Collision with Player --
  ---------------------------
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		local worlds = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Worlds")
		local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
		
		local stageClearSfx = game.assets.manager:get(assetFiles.sfxStageClear)
		
		stageClearSfx:play()
		
		local newMainMenuScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.MainMenuScreen", game)
		game.currentScreen:dispose()
		game.currentScreen = newMainMenuScreen
		game:setScreen(newMainMenuScreen)
		
		game.engine:removeAllEntities()
		game.scriptManager = luajava.newInstance("io.whitegoldlabs.wiseguys.util.ScriptManager")
		worlds:unloadWorldEntities()
	end
end

return Goal
