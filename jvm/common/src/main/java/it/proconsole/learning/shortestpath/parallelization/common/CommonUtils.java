package it.proconsole.learning.shortestpath.parallelization.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe di utilità comune per logging e configurazione base.
 */
public class CommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private CommonUtils() {
        // Utility class
    }

    /**
     * Restituisce un messaggio di benvenuto per verificare il setup.
     *
     * @return messaggio di benvenuto
     */
    public static String getWelcomeMessage() {
        logger.info("CommonUtils initialized");
        return "Welcome to Shortest Path Parallelization Common Module";
    }

    /**
     * Verifica che il modulo sia configurato correttamente.
     *
     * @return true se il setup è corretto
     */
    public static boolean isSetupValid() {
        return true;
    }
}

