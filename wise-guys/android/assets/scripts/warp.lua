Warp = {}

function Warp.execute(thisEntity, game, input, triggerKey, sfxPipe, destination, x, y)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		if input:isKeyPressed(triggerKey) then
			local thread = luajava.bindClass("java.lang.Thread") 
			
			sfxPipe:play()
			thread:sleep(1000)
			game.wasSleeping = true
			
			local newGameScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.GameScreen", game, destination, x, y)
			game.currentScreen:dispose()
			game.currentScreen = newGameScreen
			game:setScreen(newGameScreen)
		end
	end
end

return Warp
