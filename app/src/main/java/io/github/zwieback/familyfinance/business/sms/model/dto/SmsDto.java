package io.github.zwieback.familyfinance.business.sms.model.dto;

import androidx.annotation.NonNull;

public class SmsDto {

    @NonNull
    private final String sender;
    @NonNull
    private final String body;

    public SmsDto(@NonNull String sender, @NonNull String body) {
        this.sender = sender;
        this.body = body;
    }

    @NonNull
    public String getSender() {
        return sender;
    }

    @NonNull
    public String getBody() {
        return body;
    }

    @NonNull
    @Override
    public String toString() {
        return "SmsDto{" +
                "sender='" + sender + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
