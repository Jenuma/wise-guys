Box = {}

function Box.execute(box, game, contents)
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
		
		local playerVelocity = mappers.velocity:get(game.player)
	
    --------------------------------
    -- Get Intersection Rectangle --
    --------------------------------
    local intersectX = math.max(playerHitboxComponent.hitbox.x, boxHitboxComponent.hitbox.x)
    local intersectY = math.max(playerHitboxComponent.hitbox.y, boxHitboxComponent.hitbox.y)
    local intersectWidth = math.min(playerHitboxComponent.hitbox.x + playerHitboxComponent.hitbox.width, boxHitboxComponent.hitbox.x + boxHitboxComponent.hitbox.width) - intersectX
    local intersectHeight = math.min(playerHitboxComponent.hitbox.y + playerHitboxComponent.hitbox.height, boxHitboxComponent.hitbox.y + boxHitboxComponent.hitbox.height) - intersectY
	  
	  if playerVelocity.y > 0 then
  	  if intersectWidth > intersectHeight then
    		if playerHitboxComponent.hitbox.y < boxHitboxComponent.hitbox.y then
    		  local phases = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PhaseComponent")
    		  box:remove(phases)
    		
    			local animations = luajava.bindClass("io.whitegoldlabs.wiseguys.component.AnimationComponent")
    			local scripts = luajava.bindClass("io.whitegoldlabs.wiseguys.component.ScriptComponent")
    			local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
    			local arrayInstantiator = luajava.bindClass("io.whitegoldlabs.wiseguys.util.ArrayInstantiator")
    			
    			local spriteSheet = game.assets.manager:get(assetFiles.spriteSheet)
    			local emptyBoxSprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 48, 80, 16, 16)
    			local boxSpriteComponent = mappers.sprite:get(box)
    			
    			boxSpriteComponent.sprite = emptyBoxSprite
    			
    			local boxPosition = mappers.position:get(box)
  
          ----------------------
          -- Contains Connolo --
          ----------------------
          if contents == "CONNOLO" then
            local powerupAppearsSfx = game.assets.manager:get(assetFiles.sfxPowerupAppears)
            local contentsSprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 128, 0, 16, 16)
            
            powerupAppearsSfx:play()
            
            local contentsEntity = luajava.newInstance("com.badlogic.ashley.core.Entity")
            local contentsTypeComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.TypeComponent", types.Type.PICKUP)
            local contentsStateComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.StateComponent")
            local contentsPositionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", boxPosition.x, boxPosition.y)
            local contentsSpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", contentsSprite)
            local contentsHitboxComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.HitboxComponent", contentsPositionComponent.x, contentsPositionComponent.y, 16, 16)
            local contentsCollisionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.CollisionComponent", box)
            local contentsPhaseComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PhaseComponent")
            
            local scriptArgs = arrayInstantiator:getNewArray()
            scriptArgs:add(contentsEntity)
            scriptArgs:add(game)
            
            local contentsScriptComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.ScriptComponent", "scripts\\connolo.lua", scriptArgs)
            game.scriptManager:loadScript("scripts\\connolo.lua")
            
            contentsEntity:add(contentsTypeComponent)
            contentsEntity:add(contentsStateComponent)
            contentsEntity:add(contentsPositionComponent)
            contentsEntity:add(contentsSpriteComponent)
            contentsEntity:add(contentsHitboxComponent)
            contentsEntity:add(contentsCollisionComponent)
            contentsEntity:add(contentsScriptComponent)
            contentsEntity:add(contentsPhaseComponent)
            contentsSpriteComponent.sprite:setPosition(contentsPositionComponent.x, contentsPositionComponent.y)
            game.engine:addEntity(contentsEntity)
            
          -------------------
          -- Contains Coin --
          -------------------
          else
            local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
            local playModes = luajava.bindClass("com.badlogic.gdx.graphics.g2d.Animation")
            
            local contentsSprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 0, 64, 16, 16)
            local coinSfx = game.assets.manager:get(assetFiles.sfxCoin)
            local player = mappers.player:get(game.player)
            
            coinSfx:play()
            player.coins = player.coins + 1
            player.score = player.score + 200
            
            local contentsEntity = luajava.newInstance("com.badlogic.ashley.core.Entity")
            local contentsTypeComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.TypeComponent", types.Type.PRIORITY)
            local contentsStateComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.StateComponent")
            local contentsPositionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", boxPosition.x, boxPosition.y + 20)
            local contentsVelocityComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.VelocityComponent", 0, 3)
            local contentsAccelerationComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.AccelerationComponent", 0, 3)
            local contentsSpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", contentsSprite)
            local contentsHitboxComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.HitboxComponent", contentsPositionComponent.x, contentsPositionComponent.y, 16, 16)
            local contentsPhaseComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PhaseComponent")
            local contentsAnimationComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.AnimationComponent")
            
            local scriptArgs = arrayInstantiator:getNewArray()
            scriptArgs:add(contentsEntity)
            scriptArgs:add(contentsPositionComponent.y)
            
            local contentsBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\box_coin_behavior.lua", scriptArgs)
            game.scriptManager:loadScript("scripts\\box_coin_behavior.lua")
            
            local frame2 = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 16, 64, 16, 16)
            local frame3 = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 32, 64, 16, 16)
            local frame4 = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 48, 64, 16, 16)
            
            local animationFrames = arrayInstantiator:getNewArray()
            animationFrames:add(contentsSprite)
            animationFrames:add(frame2)
            animationFrames:add(frame3)
            animationFrames:add(frame4)
            
            local animation = arrayInstantiator:getNewAnimation(1/16, animationFrames, playModes.PlayMode.LOOP)
            contentsAnimationComponent.animations:put("FALLING", animation)
            
            contentsStateComponent.airborneState = states.AirborneState.FALLING
            
            contentsEntity:add(contentsTypeComponent)
            contentsEntity:add(contentsStateComponent)
            contentsEntity:add(contentsPositionComponent)
            contentsEntity:add(contentsVelocityComponent)
            contentsEntity:add(contentsAccelerationComponent)
            contentsEntity:add(contentsSpriteComponent)
            contentsEntity:add(contentsHitboxComponent)
            contentsEntity:add(contentsPhaseComponent)
            contentsEntity:add(contentsAnimationComponent)     
            contentsEntity:add(contentsBehaviorComponent)    
            contentsSpriteComponent.sprite:setPosition(contentsPositionComponent.x, contentsPositionComponent.y)
            game.engine:addEntity(contentsEntity)
          end
    			
    			local scriptArgs = arrayInstantiator:getNewArray()
    			scriptArgs:add(box)
    			scriptArgs:add(boxPosition.y)
    			
    			local boxBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\bounce_behavior.lua", scriptArgs)
    			boxBehaviorComponent.behaviorState = "BOUNCING_UP"
    			game.scriptManager:loadScript("scripts\\bounce_behavior.lua")
    			
    			box:add(boxBehaviorComponent)
    			box:remove(animations)
    			box:remove(scripts)
    		end
  		end
		end
	end
end

return Box
