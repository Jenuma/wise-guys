Box = {}

function Box.execute(thisEntity, emptyBoxSprite, sfxCoin)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		local collidingEntityHitboxComponent = mappers.hitbox:get(collidingEntity)
		local thisEntityHitboxComponent = mappers.hitbox:get(thisEntity)
	
		if collidingEntityHitboxComponent.hitbox.y < thisEntityHitboxComponent.hitbox.y then
			local collisions = luajava.bindClass("io.whitegoldlabs.wiseguys.component.CollisionComponent")
			local animations = luajava.bindClass("io.whitegoldlabs.wiseguys.component.AnimationComponent")
			local scripts = luajava.bindClass("io.whitegoldlabs.wiseguys.component.ScriptComponent")
	
			local thisEntitySpriteComponent = mappers.sprite:get(thisEntity)
	
			sfxCoin:play()
			thisEntitySpriteComponent.sprite = emptyBoxSprite
			
			thisEntity:remove(animations)
			thisEntity:remove(collisions)
			thisEntity:remove(scripts)
		end
	end
end

return Box
