package com.spring.dozen.hub.application.service;

import com.spring.dozen.hub.application.exception.ErrorCode;
import com.spring.dozen.hub.application.exception.HubException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class AddressToCoordinateService {

    @Value("${KAKAO_API_KEY}")
    private String apiKey;

    @Value("${KAKAO_BASE_URL}")
    private String baseUrl;

    public double[] getCoordinates(String address) throws HubException {

        try {
            String url = buildUrl(address);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Authorization", "KakaoAK " + apiKey);
            CloseableHttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new HubException(ErrorCode.KAKAO_API_REQUEST_FAILED);
            }

            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(jsonStr);
            double[] coordinates = new double[2];

            if (jsonObject.has("documents")) {
                JSONArray documents = jsonObject.getJSONArray("documents");

                if (!documents.isEmpty()) {
                    JSONObject document = documents.getJSONObject(0);
                    coordinates[0] = document.getDouble("y"); // 위도
                    coordinates[1] = document.getDouble("x"); // 경도
                } else {
                    throw new HubException(ErrorCode.NOT_FOUND_ADDRESS);
                }
            }
            response.close();
            httpClient.close();

            return coordinates;
        } catch (IOException e) {
            throw new HubException(ErrorCode.KAKAO_API_REQUEST_FAILED);
        }
    }

    private String buildUrl(String address) {

        Map<String, String> params = new HashMap<>();
        params.put("query", URLEncoder.encode(address, StandardCharsets.UTF_8));

        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> param : params.entrySet()) {
            urlBuilder.append(param.getKey()).append("=").append(param.getValue());
        }

        return urlBuilder.toString();
    }
}