package firered.pokemon;

import firered.gfx.sprites.Sprite;

public enum StatusEffect {
	NONE(null, "none"), POISON(new Sprite("ui/pokemon/poison_icon"), "was poisoned"), TOXIC_POISON(new Sprite("ui/pokemon/poison_icon"), "was badly poisoned"), PARALYSIS(new Sprite("ui/pokemon/paralysis_icon"), "was paralysed"), SLEEP(new Sprite("ui/pokemon/sleep_icon"), "fell asleep"), BURN(new Sprite("ui/pokemon/burn_icon"), "was burned"), FROZEN(new Sprite("ui/pokemon/frozen_icon"), "was frozen solid");

	public Sprite sprite;
	public String text;

	StatusEffect(Sprite sprite, String text) {
		this.sprite = sprite;
		this.text = text;
	}
}