Mushroom = {}

function Mushroom.execute(thisEntity)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	if collidingEntityTypeComponent.type == types.Type.OBSTACLE then
		thisEntityPositionComponent = mappers.position:get(thisEntity)
		thisEntityHitboxComponent = mappers.hitbox:get(thisEntity)
		
		thisEntityPositionComponent.y = thisEntityPositionComponent.y + 0.4
		thisEntityHitboxComponent.hitbox.y = thisEntityPositionComponent.y
	end
	
	local collisions = luajava.bindClass("io.whitegoldlabs.wiseguys.component.CollisionComponent")
	
	thisEntity:remove(collisions)
end

return Mushroom
