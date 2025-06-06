package com.example.apijsonsearch.service;

import com.example.apijsonsearch.model.Equipo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

@Service
public class SitrackService {

    @Value("${sitrack.username}")
    private String username;

    @Value("${sitrack.password}")
    private String password;

    private final String BASE_URL = "https://externalappgw.cl.sitrack.com";

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders createHeaders() {
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public Equipo buscarEquipoPorTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) return null;

        texto = texto.toUpperCase().trim();

        if (texto.length() == 6) {
            return buscarPorAssetId(texto);
        } else {
            List<Equipo> todos = obtenerTodosLosEquipos();
            for (Equipo equipo : todos) {
                if (texto.equalsIgnoreCase(equipo.getAssetName())) {
                    return buscarPorAssetId(equipo.getAssetId());
                }
            }
        }

        return null;
    }

    public Equipo buscarPorAssetId(String assetId) {
        try {
            String url = BASE_URL + "/v2/report?assetId=" + assetId;
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());

            ResponseEntity<Equipo> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Equipo.class
            );

            return response.getBody();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public List<Equipo> obtenerTodosLosEquipos() {
        try {
            String url = BASE_URL + "/v2/report";
            HttpEntity<String> entity = new HttpEntity<>(createHeaders());

            ResponseEntity<Equipo[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Equipo[].class
            );

            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (HttpClientErrorException e) {
            return new ArrayList<>();
        }
    }
}
