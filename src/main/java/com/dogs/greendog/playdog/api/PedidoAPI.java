package com.dogs.greendog.playdog.api;

import com.dogs.greendog.playdog.domain.Cliente;
import com.dogs.greendog.playdog.domain.Item;
import com.dogs.greendog.playdog.domain.Pedido;
import com.dogs.greendog.playdog.repository.ClienteRepository;
import com.dogs.greendog.playdog.repository.ItemRepository;
import com.dogs.greendog.playdog.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PedidoAPI {

	private final PedidoRepository pedidoRepository;
	private final ClienteRepository clienteRepository;
	private final ItemRepository itemRepository;

	public PedidoAPI(PedidoRepository pedidoRepository,ClienteRepository clienteRepository,ItemRepository itemRepository) {
		this.pedidoRepository = pedidoRepository;
		this.clienteRepository = clienteRepository;
		this.itemRepository = itemRepository;
	}

	@PostMapping("/pedido")
	public Pedido fazPedido(@RequestBody NovoPedido novoPedido) {
		
		Pedido pedido = new Pedido();
		Optional<Cliente> clienteOpt = clienteRepository.findById(novoPedido.getIdCliente());
		pedido.setCliente(clienteOpt.orElseThrow(() -> new RuntimeException("Possivel cliente nulo")));
		pedido.setData(new Date());
		pedido.setValorTotal(novoPedido.getValorTotal());
		
		List<Item> itens = new ArrayList<>();
		for (Long idItem : novoPedido.getItensId()) {
			Optional<Item> itemOpt = itemRepository.findById(idItem);
			Item item = itemOpt.orElseThrow(() -> new RuntimeException("Possivel item nulo"));
			itens.add(item);
		}
		pedido.setItens(itens);
		pedido.setStatus(FluxoPedido.CHEGOU_NA_COZINHA.name());
		
		pedidoRepository.save(pedido);
		
		return pedido;
	}

	@GetMapping("/pedido/{id}")
	public Pedido buscaPedido(@PathVariable Long id) {

		Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
		if (pedidoOpt.isPresent()) {
			return pedidoOpt.get();
		} else {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT,FluxoPedido.NAO_ENCONTRADO.name());
		}
	}

	@DeleteMapping("/pedido/{id}")
	public void cancelaPedido(@PathVariable Long id) {
		
		Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
		Pedido pedido = pedidoOpt.orElseThrow(() -> new RuntimeException("Possivel pedido nulo"));

		pedido.setStatus(FluxoPedido.CANCELADO.name());
		pedidoRepository.save(pedido);
		pedidoRepository.flush();
		 
	}

	@GetMapping("/pedido/all")
	public List<Pedido> buscaTudo() {

		List<Pedido> pedidoLista = pedidoRepository.findAll();
		if (pedidoLista.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}

		return pedidoLista;
	}
	
}
