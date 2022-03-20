package com.dogs.greendog.playdog.config;

import com.dogs.greendog.playdog.dto.Notificacao;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class ProdNotificacaoConfig implements Notificacao {

	@Override
	public boolean envioAtivo() {
		return true;
	}
 
}
