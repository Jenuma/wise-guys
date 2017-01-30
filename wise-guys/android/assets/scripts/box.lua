Box = {}

function Box.execute(thisEntity, game, emptyBoxSprite, contentsSprite, scriptArgs)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		local collidingEntityHitboxComponent = mappers.hitbox:get(collidingEntity)
		local thisEntityHitboxComponent = mappers.hitbox:get(thisEntity)
	
		if collidingEntityHitboxComponent.hitbox.y < thisEntityHitboxComponent.hitbox.y then
			local animations = luajava.bindClass("io.whitegoldlabs.wiseguys.component.AnimationComponent")
			local scripts = luajava.bindClass("io.whitegoldlabs.wiseguys.component.ScriptComponent")
			local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
			
			local thisEntitySpriteComponent = mappers.sprite:get(thisEntity)
			local thisEntityTypeComponent = mappers.type:get(thisEntity)
			local powerupAppearsSfx = game.assets.manager:get(assetFiles.sfxPowerupAppears)
	
			powerupAppearsSfx:play()
			thisEntitySpriteComponent.sprite = emptyBoxSprite
			
			local thisEntityPosition = mappers.position:get(thisEntity)
			
			local contents = luajava.newInstance("com.badlogic.ashley.core.Entity")
			local contentsTypeComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.TypeComponent", types.Type.PICKUP)
			local contentsStateComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.StateComponent")
			local contentsPositionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", thisEntityPosition.x, thisEntityPosition.y)
			local contentsSpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", contentsSprite)
			local contentsHitboxComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.HitboxComponent", contentsPositionComponent.x, contentsPositionComponent.y, 16, 16)
			local contentsCollisionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.CollisionComponent", thisEntity)
			
			scriptArgs:add(contents)
			scriptArgs:add(game)
			local contentsScriptComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.ScriptComponent", false, "scripts\\mushroom.lua", scriptArgs)
			game.scriptManager:loadScript("scripts\\mushroom.lua")
			
			contents:add(contentsTypeComponent)
			contents:add(contentsStateComponent)
			contents:add(contentsPositionComponent)
			contents:add(contentsSpriteComponent)
			contents:add(contentsHitboxComponent)
			contents:add(contentsCollisionComponent)
			contents:add(contentsScriptComponent)
			contentsSpriteComponent.sprite:setPosition(contentsPositionComponent.x, contentsPositionComponent.y)
			game.engine:addEntity(contents)
			
			thisEntity:remove(animations)
			thisEntity:remove(scripts)
			thisEntityTypeComponent.type = types.Type.OBSTACLE
		end
	end
end

return Box
