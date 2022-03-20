package com.dogs.greendog.playdog.carga;

import com.dogs.greendog.playdog.api.FluxoPedido;
import com.dogs.greendog.playdog.domain.Cliente;
import com.dogs.greendog.playdog.domain.Item;
import com.dogs.greendog.playdog.domain.Pedido;
import com.dogs.greendog.playdog.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RepositoryTest 
 implements ApplicationRunner 
{

	private static final long ID_CLIENTE_FERNANDO = 11L;
	private static final long ID_CLIENTE_ZE_PEQUENO = 22L;
	
	private static final long ID_ITEM1 = 100L;
	private static final long ID_ITEM2 = 101L;
	private static final long ID_ITEM3 = 102L;
	
	private static final long ID_PEDIDO1 = 1000L;
	private static final long ID_PEDIDO2 = 1001L;
	private static final long ID_PEDIDO3 = 1002L;
	
	@Autowired
    private ClienteRepository clienteRepository;
	
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

    	log.info(">>> Iniciando carga de dados...");
    	Cliente fernando = new Cliente(ID_CLIENTE_FERNANDO,"ELvis Madeira","Sampa");
    	Cliente zePequeno = new Cliente(ID_CLIENTE_ZE_PEQUENO,"Zé Pequeno","Cidade de Deus");    	
    	
    	Item dog1 = new Item(ID_ITEM1,"Green Dog tradicional",25d);
    	Item dog2 = new Item(ID_ITEM2,"Green Dog tradicional picante",27d);
		Item dog3 = new Item(ID_ITEM3,"Green Dog max salada",30d);
    	
    	List<Item> listaPedidoFernando1 = new ArrayList<>();
    	listaPedidoFernando1.add(dog1);

    	List<Item> listaPedidoZePequeno1 = new ArrayList<>();
    	listaPedidoZePequeno1.add(dog2);
    	listaPedidoZePequeno1.add(dog3);
    	
    	Pedido pedidoDoFernando = new Pedido(ID_PEDIDO1,fernando,listaPedidoFernando1,dog1.getPreco(), FluxoPedido.CHEGOU_NA_COZINHA.name() );
    	fernando.novoPedido(pedidoDoFernando);
    	
    	Pedido pedidoDoZepequeno = new Pedido(ID_PEDIDO2,zePequeno,listaPedidoZePequeno1, dog2.getPreco()+dog3.getPreco(),FluxoPedido.CHEGOU_NA_COZINHA.name());
    	zePequeno.novoPedido(pedidoDoZepequeno);

		log.info(">>> Pedido 1 - Fernando : "+ pedidoDoFernando);
		log.info(">>> Pedido 2 - Ze Pequeno: "+ pedidoDoZepequeno);
    	
       
		clienteRepository.saveAndFlush(zePequeno);
		log.info(">>> Gravado cliente 2: "+zePequeno);

		List<Item> listaPedidoFernando2 = new ArrayList<>();
		listaPedidoFernando2.add(dog2);
    	Pedido pedido2DoFernando  = new Pedido(ID_PEDIDO3,fernando,listaPedidoFernando2,dog2.getPreco(),FluxoPedido.CHEGOU_NA_COZINHA.name());
    	fernando.novoPedido(pedido2DoFernando);
    	clienteRepository.saveAndFlush(fernando);
		log.info(">>> Pedido 2 - Fernando : "+ pedido2DoFernando);
		log.info(">>> Gravado cliente 1: "+fernando);
		
    }
 
}
