Mushroom = {}

function Mushroom.execute(thisEntity)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	local collidingEntityHitboxComponent = mappers.hitbox:get(collidingEntity)
	local thisEntityHitboxComponent = mappers.hitbox:get(thisEntity)
	
	local thisEntityStateComponent = mappers.state:get(thisEntity)
	local thisEntityPositionComponent = mappers.position:get(thisEntity)
	
	-----------------------------
	-- Collision with Obstacle --
	-----------------------------
	if collidingEntityTypeComponent.type == types.Type.OBSTACLE then
	
		-----------------------
		-- Mushroom Spawning --
		-----------------------
		if thisEntityStateComponent.motionState == states.MotionState.STILL then
			thisEntityPositionComponent.y = thisEntityPositionComponent.y + 0.4
			thisEntityHitboxComponent.hitbox.y = thisEntityPositionComponent.y
		else
			----------------------
			-- Mushroom Falling --
			----------------------
			if thisEntityStateComponent.airborneState == states.AirborneState.FALLING then
				thisEntityPositionComponent.y = collidingEntityHitboxComponent.hitbox.y + collidingEntityHitboxComponent.hitbox.height
				thisEntityHitboxComponent.hitbox.y = thisEntityPositionComponent.y
				
				thisEntityStateComponent.airborneState = states.AirborneState.GROUNDED
				
			---------------------
			-- Mushroom Moving --
			---------------------
			else
				local thisEntityVelocityComponent = mappers.velocity:get(thisEntity)
				thisEntityVelocityComponent.x = 0 - thisEntityVelocityComponent.x
			
				if thisEntityStateComponent.directionState == states.DirectionState.RIGHT then
					thisEntityStateComponent.directionState = states.DirectionState.LEFT
				else
					thisEntityStateComponent.directionState = states.DirectionState.RIGHT
				end
			end
		end
	end
	
	--------------------------------
	-- Mushroom Finished Spawning --
	--------------------------------
	if not collidingEntityHitboxComponent.hitbox:overlaps(thisEntityHitboxComponent.hitbox) then
		local thisEntityVelocityComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.VelocityComponent", 25, 0)
		local thisEntityAccelerationComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.AccelerationComponent", 0, 0)
		
		thisEntity:add(thisEntityVelocityComponent)
		thisEntity:add(thisEntityAccelerationComponent)
		
		thisEntityStateComponent.motionState = states.MotionState.MOVING
	end
end

return Mushroom
