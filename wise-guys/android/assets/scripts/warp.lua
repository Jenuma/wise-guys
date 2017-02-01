Warp = {}

function Warp.execute(warpObject, game, triggerKey, destination, x, y)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	local gdx = luajava.bindClass("com.badlogic.gdx.Gdx")
	
	local collisionComponent = mappers.collision:get(warpObject)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	---------------------------
	-- Collision with Player --
	---------------------------
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		----------------------
		-- Start Warp Event --
		----------------------
		if gdx.input:isKeyPressed(triggerKey) then
		  local arrayInstantiator = luajava.bindClass("io.whitegoldlabs.wiseguys.util.ArrayInstantiator")
		  
		  local scriptArgs = arrayInstantiator:getNewArray()
		  scriptArgs:add(game)
		  scriptArgs:add(triggerKey)
		  scriptArgs:add(destination)
		  scriptArgs:add(x)
		  scriptArgs:add(y)
		  
		  local playerBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\warp_behavior.lua", scriptArgs)
      playerBehaviorComponent.behaviorState = "WARP_STARTED"
      game.scriptManager:loadScript("scripts\\warp_behavior.lua")
      collidingEntity:add(playerBehaviorComponent)
		end
	end
end

return Warp
