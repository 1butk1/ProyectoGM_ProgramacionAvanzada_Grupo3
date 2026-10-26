package com.mygdx.game.template;

import com.mygdx.game.logica.GameManager;
import com.mygdx.game.personaje.Tarro;

public interface EfectoCaida {
	void applyEffect(Tarro tarro, GameManager gestor);
}
