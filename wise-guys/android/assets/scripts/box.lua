Box = {}

function Box.execute(entity, playerHitbox, boxHitbox, animationComponent, boxSpriteComponent, emptyBoxSprite, sfxCoin)
	if playerHitbox.y < boxHitbox.y then
		entity:remove(animationComponent)
		sfxCoin:play()
		boxSpriteComponent.sprite = emptyBoxSprite
	end
end

return Box
