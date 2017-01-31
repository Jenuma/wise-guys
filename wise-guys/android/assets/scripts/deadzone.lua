Deadzone = {}

function Deadzone.execute(deadzone, game)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(deadzone)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	---------------------------
	-- Collision with Player --
	---------------------------
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
	  local arrayInstantiator = luajava.bindClass("io.whitegoldlabs.wiseguys.util.ArrayInstantiator")
	  local scripts = luajava.bindClass("io.whitegoldlabs.wiseguys.component.ScriptComponent")
	
	  local playerPositionComponent = mappers.position:get(collidingEntity)
	
	  local scriptArgs = arrayInstantiator:getNewArray()
	  scriptArgs:add(game)
	  scriptArgs:add(playerPositionComponent.y)
	
	  local playerBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\jules_death_behavior.lua", scriptArgs)
    playerBehaviorComponent.behaviorState = "DYING"
    game.scriptManager:loadScript("scripts\\jules_death_behavior.lua")
    
    game.player:add(playerBehaviorComponent)
	
	  deadzone:remove(scripts)
	
		--Jules_death.execute(game)
	end
end

return Deadzone
