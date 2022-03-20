package com.dogs.greendog.playdog.rest;

import com.dogs.greendog.playdog.domain.Estoque;
import com.dogs.greendog.playdog.domain.Item;
import com.dogs.greendog.playdog.domain.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class AtualizaEstoqueService {
	
	public void send(Pedido pedido) {
		
		RestTemplate restTemplate = new RestTemplate();
		String resourceUrl = "http://localhost:9000/api/atualiza";
		
		for (Item item : pedido.getItens()) {
			
			log.info("Enviando requisicao - atualizando estoque - [ "+item.getNome()+" ] ...");
			
			Estoque estoque = new Estoque(item.getId(),1l);
			
			HttpEntity<Estoque> requestEstoque = new HttpEntity<>(estoque);
			
			String responseEstoque = restTemplate.postForObject(resourceUrl, requestEstoque, String.class);

			log.info("Resposta da atualizaçao de estoque: "+responseEstoque);
			
		}
		
	}

}