package com.nammapg.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStorage.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(5)));
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        OtpData otpData = otpStorage.get(email);
        if (otpData == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            otpStorage.remove(email);
            return false;
        }
        boolean isValid = otpData.getOtp().equals(otp);
        if (isValid) {
            otpStorage.remove(email);
        }
        return isValid;
    }

    private static class OtpData {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }
}
