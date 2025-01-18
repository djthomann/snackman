package de.hsrm.mi.swt.projekt.snackman.communication.events.backendToBackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import de.hsrm.mi.swt.projekt.snackman.logic.GameState;

@Component
public class ScareEventListener implements ApplicationListener <ScareEvent> {

    Logger logger = LoggerFactory.getLogger(EatEventListener.class);

    @Override
    public void onApplicationEvent(ScareEvent event) {
        GameState gameState = event.getGameManager().getGameById(event.getGameId()).getGameState();
        gameState.addChangedSnackMan(event.getScaredSnackMan());
    }

}
