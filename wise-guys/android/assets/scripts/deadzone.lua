Deadzone = {}

function Deadzone.execute(thisEntity, game)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	---------------------------
  -- Collision with Player --
  ---------------------------
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		Jules_death.execute(game)
	end
end

return Deadzone
