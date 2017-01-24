Box = {}

function Box.execute(playerHitbox, boxHitbox, sfxCoin)
	if playerHitbox.y < boxHitbox.y then
		sfxCoin:play()
	end
end

return Box
