package br.com.wandaymo.caju.authorizer.log;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogPattern extends ch.qos.logback.classic.PatternLayout {

    @Override
    public void start() {
        super.start();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        return super.doLayout(event);
    }

}
