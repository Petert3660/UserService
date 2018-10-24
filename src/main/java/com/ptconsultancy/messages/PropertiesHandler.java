package com.ptconsultancy.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class PropertiesHandler {

    private ResourceBundleMessageSource messageSource;

    @Autowired
    public PropertiesHandler(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getProperty(String key) {

        String sources[] = key.split("\\.");
        String source = sources[0];

        setPropertiesSource(source);
        return messageSource.getMessage(key, null, null);
    }

    public String getProperty(String key, String[] params) {

        String sources[] = key.split("\\.");
        String source = sources[0];

        setPropertiesSource(source);
        Object[] args = params;
        return messageSource.getMessage(key, args, null);
    }

    private void setPropertiesSource (String newBaseName){
        messageSource.setBasename(newBaseName);
    }
}
