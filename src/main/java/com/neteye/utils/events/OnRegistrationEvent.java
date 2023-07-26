package com.neteye.utils.events;

import com.neteye.persistence.entities.User;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnRegistrationEvent extends ApplicationEvent {
    @Getter
    private final String appUrl;
    @Getter
    private final Locale locale;
    @Getter
    private final User user;

    public OnRegistrationEvent(User user, Locale locale, String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }
}
