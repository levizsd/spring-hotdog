package com.dogs.greendog.playdog.rest;

import com.dogs.greendog.playdog.domain.Cliente;
import com.dogs.greendog.playdog.domain.Item;
import com.dogs.greendog.playdog.domain.Pedido;
import com.dogs.greendog.playdog.dto.RespostaDTO;
import com.dogs.greendog.playdog.repository.ClienteRepository;
import com.dogs.greendog.playdog.repository.ItemRepository;
import com.dogs.greendog.playdog.service.AtualizaEstoque;
import com.dogs.greendog.playdog.util.EnviaNotificacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController 
public class NovoPedidoController {

	
	@Autowired
	public NovoPedidoController(ClienteRepository clienteRepository, ItemRepository itemRepository, EnviaNotificacao enviaNotificacao, AtualizaEstoque atualizaEstoque) {
		this.clienteRepository =clienteRepository;
		this.itemRepository=itemRepository;
		this.enviaNotificacao = enviaNotificacao;
		this.atualizaEstoque = atualizaEstoque;
	}

	private final ClienteRepository clienteRepository;
	private final ItemRepository itemRepository;
	private final EnviaNotificacao enviaNotificacao;
	private final AtualizaEstoque atualizaEstoque;

	@GetMapping("/rest/pedido/novo/{clienteId}/{listaDeItens}")
	public RespostaDTO novo(@PathVariable("clienteId") Long clienteId, @PathVariable("listaDeItens") String listaDeItens) {

		RespostaDTO dto = new RespostaDTO();

		try {
			
			Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
			Cliente c = clienteOpt.orElseThrow(() -> new RuntimeException("Possivel cliente nulo"));

			String[] listaDeItensID = listaDeItens.split(",");

			Pedido pedido = new Pedido();

			double valorTotal = 0;
			List<Item> itensPedidos = new ArrayList<>();

			for (String itemId : listaDeItensID) {
				
				Optional<Item> itemOpt = itemRepository.findById(Long.parseLong(itemId));
				Item item = itemOpt.orElseThrow(() -> new RuntimeException("Possivel cliente nulo"));
				 
				itensPedidos.add(item);
				valorTotal += item.getPreco();
			}
			pedido.setItens(itensPedidos);
			pedido.setValorTotal(valorTotal);
			pedido.setData(new Date());
			pedido.setCliente(c);
			c.getPedidos().add(pedido);

			this.clienteRepository.saveAndFlush(c);
			
			enviaNotificacao.enviaEmail(c,pedido);
			
			List<Long> pedidosID = new ArrayList<>();
			for (Pedido p : c.getPedidos()) {
				pedidosID.add(p.getId());
			}

			Long ultimoPedido = Collections.max(pedidosID);

			// atualiza estoque
			atualizaEstoque.processar(pedido);
			
			dto = new RespostaDTO(ultimoPedido,valorTotal,"Pedido efetuado com sucesso");

		} catch (Exception e) {
			dto.setMensagem("Erro: " + e.getMessage());
		}
		return dto;

	}

}
