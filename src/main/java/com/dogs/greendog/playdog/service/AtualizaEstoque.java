package com.dogs.greendog.playdog.service;

import com.dogs.greendog.playdog.domain.Pedido;
import com.dogs.greendog.playdog.rest.AtualizaEstoqueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class AtualizaEstoque {

	@Autowired
	private AtualizaEstoqueService atualizaEstoqueService;
	
	public void processar(Pedido pedido) {
		
		try {
			// atualiza estoque 
			log.info("== Atualizar Estoque ==");
			atualizaEstoqueService.send(pedido);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
