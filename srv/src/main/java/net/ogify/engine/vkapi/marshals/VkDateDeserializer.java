package net.ogify.engine.vkapi.marshals;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class which is used for parsing birth date field in vk responses
 */
public class VkDateDeserializer extends JsonDeserializer<Date> {
    private final static Logger logger = Logger.getLogger(VkDateDeserializer.class);

    private static final SimpleDateFormat vkDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        Date parsed = null;
        try {
            parsed = vkDateFormat.parse(jsonParser.getText());
        } catch (ParseException e) {
            logger.warn("Can't parse date from vk.", e);
        }

        return parsed;
    }
}
