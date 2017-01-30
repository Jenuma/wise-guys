Box = {}

function Box.execute(box, game, emptyBoxSprite, contentsSprite, scriptArgs)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(box)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	---------------------------
	-- Collision With Player --
	---------------------------
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		local playerHitboxComponent = mappers.hitbox:get(collidingEntity)
		local boxHitboxComponent = mappers.hitbox:get(box)
	
    --------------------------------
    -- Get Intersection Rectangle --
    --------------------------------
    local intersectX = math.max(playerHitboxComponent.hitbox.x, boxHitboxComponent.hitbox.x)
    local intersectY = math.max(playerHitboxComponent.hitbox.y, boxHitboxComponent.hitbox.y)
    local intersectWidth = math.min(playerHitboxComponent.hitbox.x + playerHitboxComponent.hitbox.width, boxHitboxComponent.hitbox.x + boxHitboxComponent.hitbox.width) - intersectX
    local intersectHeight = math.min(playerHitboxComponent.hitbox.y + playerHitboxComponent.hitbox.height, boxHitboxComponent.hitbox.y + boxHitboxComponent.hitbox.height) - intersectY
	  
	  if intersectWidth > intersectHeight then
  		if playerHitboxComponent.hitbox.y < boxHitboxComponent.hitbox.y then
  			local animations = luajava.bindClass("io.whitegoldlabs.wiseguys.component.AnimationComponent")
  			local scripts = luajava.bindClass("io.whitegoldlabs.wiseguys.component.ScriptComponent")
  			local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
  			
  			local boxSpriteComponent = mappers.sprite:get(box)
  			local boxTypeComponent = mappers.type:get(box)
  			local powerupAppearsSfx = game.assets.manager:get(assetFiles.sfxPowerupAppears)
  	    
  			powerupAppearsSfx:play()
  			boxSpriteComponent.sprite = emptyBoxSprite
  			
  			local boxPosition = mappers.position:get(box)
  			
  			local contents = luajava.newInstance("com.badlogic.ashley.core.Entity")
  			local contentsTypeComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.TypeComponent", types.Type.PICKUP)
  			local contentsStateComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.StateComponent")
  			local contentsPositionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", boxPosition.x, boxPosition.y)
  			local contentsSpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", contentsSprite)
  			local contentsHitboxComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.HitboxComponent", contentsPositionComponent.x, contentsPositionComponent.y, 16, 16)
  			local contentsCollisionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.CollisionComponent", box)
  			
  			scriptArgs:add(contents)
  			scriptArgs:add(game)
  			local contentsScriptComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.ScriptComponent", false, "scripts\\connolo.lua", scriptArgs)
  			game.scriptManager:loadScript("scripts\\connolo.lua")
  			
  			contents:add(contentsTypeComponent)
  			contents:add(contentsStateComponent)
  			contents:add(contentsPositionComponent)
  			contents:add(contentsSpriteComponent)
  			contents:add(contentsHitboxComponent)
  			contents:add(contentsCollisionComponent)
  			contents:add(contentsScriptComponent)
  			contentsSpriteComponent.sprite:setPosition(contentsPositionComponent.x, contentsPositionComponent.y)
  			game.engine:addEntity(contents)
  			
  			scriptArgs:clear()
  			scriptArgs:add(box)
  			scriptArgs:add(boxPosition.y)
  			local boxBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\box_behavior.lua", scriptArgs)
  			boxBehaviorComponent.behaviorState = "BOUNCING_UP"
  			game.scriptManager:loadScript("scripts\\box_behavior.lua")
  			
  			box:add(boxBehaviorComponent)
  			box:remove(animations)
  			box:remove(scripts)
  			boxTypeComponent.type = types.Type.OBSTACLE
  		end
		end
	end
end

return Box
