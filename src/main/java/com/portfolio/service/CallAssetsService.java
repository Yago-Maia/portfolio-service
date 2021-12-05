package com.portfolio.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.data.vo.AssetApiVO;
import com.portfolio.data.vo.AssetVO;
import com.portfolio.exception.ComunicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static com.portfolio.constants.Messages.BAD_REQUEST_ASSETS_API;

@Service
public class CallAssetsService {
    static String webService = "https://pucminas-ms-pytrader.herokuapp.com/api/stock";
    private final AssetService assetService;

    @Autowired
    public CallAssetsService(AssetService assetService) {
        this.assetService = assetService;
    }

    public List<AssetVO> reloadAssets() throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(webService);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        } catch (Exception exception) {
            throw new ComunicationException(BAD_REQUEST_ASSETS_API);
        }

        ObjectMapper mapper = new ObjectMapper();
        List<AssetApiVO> assetApiVOList = Arrays.asList(mapper.readValue(result.toString(), AssetApiVO[].class));

        return assetService.updateAll(assetApiVOList);
    }
}
