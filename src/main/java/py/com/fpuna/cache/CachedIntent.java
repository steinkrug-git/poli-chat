package py.com.fpuna.cache;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import py.com.fpuna.model.collection.IntentDocument;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CachedIntent {

    private List<IntentDocument> value;
    private LocalDateTime expiresAt;
    private final Duration duration;

    public CachedIntent(@Value("${intent.cache.duration.minutes}") long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

    public boolean isExpired() {
        return value == null || LocalDateTime.now().isAfter(expiresAt);
    }

    public List<IntentDocument> getValue() {
        return value;
    }

    public void update(List<IntentDocument> newValue) {
        this.value = newValue;
        this.expiresAt = LocalDateTime.now().plus(duration);
    }
}
