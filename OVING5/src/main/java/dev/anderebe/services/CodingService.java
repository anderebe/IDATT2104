package dev.anderebe.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodingService {

    private static final Logger logger = LoggerFactory.getLogger(CodingService.class);

    public static String run(String code, String language) {
        logger.info("Request received to run code in " + language);
        logger.info("Code: " + code);
        return "This is the result of running the code in " + language;
    }
    
}
