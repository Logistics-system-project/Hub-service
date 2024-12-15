package com.spring.dozen.hub.application.service;

import com.spring.dozen.hub.presentation.dto.GeminiRequest;
import com.spring.dozen.hub.presentation.dto.GeminiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class GeminiService {

    @Qualifier("geminiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    // 허브 이동정보 탐색
    public String findHubMovement(GeminiRequest request) {
        String requestUrl = apiUrl + "?key=" + apiKey;

        GeminiResponse response = restTemplate.postForObject(requestUrl, request, GeminiResponse.class);

        log.info("GeminiResponse: {}", response.candidates().get(0).content().parts().get(0).text());

        return response.candidates().get(0).content().parts().get(0).text();
    }

    // Gemini API 호출
    public String callGeminiAPI(List<String> addresses) {
        String query = "허브를 순서대로 이동할 때의 총 소요시간과 총 이동거리를 계산해주세요.";
        GeminiRequest geminiRequest = GeminiRequest.of(query, addresses);
        return findHubMovement(geminiRequest);
    }

    // 총 소요 시간 추출
    public int extractTotalTime(String response) {
        String timePattern = "(?s)총 소요시간:\\s*약\\s*(\\d+)시간\\s*(\\d+)?분?";
        Pattern timeRegex = Pattern.compile(timePattern);
        Matcher timeMatcher = timeRegex.matcher(response);

        int hours = 0;
        int minutes = 0;
        if (timeMatcher.find()) {
            hours = Integer.parseInt(timeMatcher.group(1));
            // 분이 존재할 경우에만 추출
            if (timeMatcher.group(2) != null) {
                minutes = Integer.parseInt(timeMatcher.group(2));
            }
        }

        return hours * 60 + minutes;
    }

    // 총 이동 거리 추출
    public int extractTotalDistance(String response) {
        String distancePattern = "(?s)총 이동거리:\\s*약\\s*(\\d+)km";
        Pattern distanceRegex = Pattern.compile(distancePattern);
        Matcher distanceMatcher = distanceRegex.matcher(response);

        if (distanceMatcher.find()) {
            return Integer.parseInt(distanceMatcher.group(1));
        }

        return 0;
    }
}