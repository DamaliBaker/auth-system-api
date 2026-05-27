package ca.sheridancollege.bakerdam.authsystemapi.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private final Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCKOUT_MINUTES = 15;

    public void recordFailedAttempt(String email, String ipAddress) {
        recordFailedAttemptByKey(emailKey(email));
        recordFailedAttemptByKey(ipKey(ipAddress));
    }

    private void recordFailedAttemptByKey(String key) {
        LoginAttempt attempt = attempts.getOrDefault(
                key,
                new LoginAttempt(0, null)
        );

        int newAttemptCount = attempt.failedAttemptCount() + 1;
        LocalDateTime blockedUntil = attempt.blockedUntil();

        if (newAttemptCount >= MAX_ATTEMPTS) {
            blockedUntil = LocalDateTime.now().plusMinutes(LOCKOUT_MINUTES);
        }

        attempts.put(key, new LoginAttempt(newAttemptCount, blockedUntil));
    }

    public boolean isBlocked(String email, String ipAddress) {
        return isBlockedByKey(emailKey(email)) || isBlockedByKey(ipKey(ipAddress));
    }

    private boolean isBlockedByKey(String key) {
        LoginAttempt attempt = attempts.get(key);

        if (attempt == null || attempt.blockedUntil() == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(attempt.blockedUntil())) {
            attempts.remove(key);
            return false;
        }

        return true;
    }

    public void clearEmailAttempts(String email) {
        attempts.remove(emailKey(email));
    }

    private String emailKey(String email) {
        return "email:" + email.toLowerCase().trim();
    }

    private String ipKey(String ipAddress) {
        return "ip:" + ipAddress;
    }

    private record LoginAttempt(int failedAttemptCount, LocalDateTime blockedUntil) {}
}
