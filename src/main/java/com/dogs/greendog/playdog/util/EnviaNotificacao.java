package com.dogs.greendog.playdog.util;

import com.dogs.greendog.playdog.domain.Cliente;
import com.dogs.greendog.playdog.domain.Pedido;
import com.dogs.greendog.playdog.dto.Notificacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnviaNotificacao {

    @Autowired
	Notificacao notificacao;
    
	public void enviaEmail(Cliente cliente, Pedido pedido) {

		log.info("Enviar notificacao para "+cliente.getNome() + " - pedido $"+pedido.getValorTotal());
		
		if (notificacao.envioAtivo()) {
			
			/*
			     codigo de envio
			 */

			log.info("Notificacao enviada!");
			
		} else {

			log.info("Notificacao desligada!");
		
		}
	}
	
	
}
