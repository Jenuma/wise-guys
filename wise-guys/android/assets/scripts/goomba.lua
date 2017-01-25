Goomba = {}

function Goomba.execute(thisEntity, sfxStomp, game, sfxJulesDeath)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		local collidingEntityHitboxComponent = mappers.hitbox:get(collidingEntity)
		local thisEntityHitboxComponent = mappers.hitbox:get(thisEntity)
	
		--------------------------------
		-- Get Intersection Rectangle --
		--------------------------------
		local intersectX = math.max(collidingEntityHitboxComponent.hitbox.x, thisEntityHitboxComponent.hitbox.x)
		local intersectY = math.max(collidingEntityHitboxComponent.hitbox.y, thisEntityHitboxComponent.hitbox.y)
		local intersectWidth = math.min(collidingEntityHitboxComponent.hitbox.x + collidingEntityHitboxComponent.hitbox.width, thisEntityHitboxComponent.hitbox.x + thisEntityHitboxComponent.hitbox.width) - intersectX
		local intersectHeight = math.min(collidingEntityHitboxComponent.hitbox.y + collidingEntityHitboxComponent.hitbox.height, thisEntityHitboxComponent.hitbox.y + thisEntityHitboxComponent.hitbox.height) - intersectY
		
		if intersectWidth > intersectHeight then
			if collidingEntityHitboxComponent.hitbox.y > thisEntityHitboxComponent.hitbox.y then
				local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
				local collisions = luajava.bindClass("io.whitegoldlabs.wiseguys.component.CollisionComponent")
				
				local playerVelocityComponent = mappers.velocity:get(collidingEntity)
				local thisEntityStateComponent = mappers.state:get(thisEntity)
				
				sfxStomp:play()
				playerVelocityComponent.y = 430
				
				thisEntityStateComponent.enabledState = states.EnabledState.DISABLED
				thisEntity:remove(collisions)
			end
		else
			Jules_death.execute(game, sfxJulesDeath)
		end
	end
end

return Goomba
