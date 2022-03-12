package com.dogs.greendog.playdog.service;

import com.dogs.greendog.playdog.domain.CEP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ValidaCEPEntrega {

    private static final String LOCALIDADE_FIXA = "Aracaju";

    private static final String UF_FIXA = "SE";

    private static final String URL_VIACEP1 = "https://viacep.com.br/ws/";

    private static final String URL_VIACEP2 = "/json/";

    public boolean processa(String cep) {

        boolean resultado = false;

        String urlBuscaCEP = URL_VIACEP1 + cep + URL_VIACEP2;

        log.info(" buscando CEP em [" + urlBuscaCEP + "]");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<CEP> responseCEP = restTemplate.getForEntity(urlBuscaCEP, CEP.class);

        log.info("Resultado GET CEP = [" + responseCEP + "]");

        CEP wsCEP = responseCEP.getBody();
        if (wsCEP != null && (wsCEP.getUf().equals(UF_FIXA) && wsCEP.getLocalidade().equals(LOCALIDADE_FIXA))) {
            resultado = true;

        }

        log.info(" CEP [" + cep + "] valido ? ", resultado);

        return resultado;
    }
}
