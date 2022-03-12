
package com.dogs.greendog.playdog.controller;

import com.dogs.greendog.playdog.domain.Cliente;
import com.dogs.greendog.playdog.domain.Item;
import com.dogs.greendog.playdog.domain.Pedido;
import com.dogs.greendog.playdog.repository.ClienteRepository;
import com.dogs.greendog.playdog.repository.ItemRepository;
import com.dogs.greendog.playdog.repository.PedidoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

	public static final String NAO_VEIO_NADA_AQUI = "Não veio nada aqui";
	private final PedidoRepository pedidoRepository;
	private final ItemRepository itemRepository;
	private final ClienteRepository clienteRepository;
	private static final String ITEM_URI = "pedidos/";

	public PedidoController(PedidoRepository pedidoRepository,ItemRepository itemRepository,ClienteRepository clienteRepository) {
		this.pedidoRepository = pedidoRepository;
		this.itemRepository = itemRepository;
		this.clienteRepository = clienteRepository;
	}

	@GetMapping("/")
	public ModelAndView list() {
		Iterable<Pedido> pedidos = this.pedidoRepository.findAll();
		return new ModelAndView(ITEM_URI + "list","pedidos",pedidos);
	}

	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Pedido pedido) {
		return new ModelAndView(ITEM_URI + "view","pedido",pedido);
	}

	@GetMapping("/novo")
	public ModelAndView createForm(@ModelAttribute Pedido pedido) {

		Map<String,Object> model = new HashMap<>();
		model.put("todosItens",itemRepository.findAll());
		model.put("todosClientes",clienteRepository.findAll());
		return new ModelAndView(ITEM_URI + "form",model);
		 
	}

	@PostMapping(params = "form")
	public ModelAndView create(@Valid Pedido pedido,BindingResult result,RedirectAttributes redirect) {
		if (result.hasErrors()) { return new ModelAndView(ITEM_URI + "form","formErrors",result.getAllErrors()); }

		if (pedido.getId() != null) {
			
			Optional<Pedido> pedidoParaAlterarOpt = pedidoRepository.findById(pedido.getId());
			Pedido pedidoParaAlterar = pedidoParaAlterarOpt.orElseThrow(() -> new RuntimeException(NAO_VEIO_NADA_AQUI));
			
			Optional<Cliente> clienteOpt = clienteRepository.findById(pedidoParaAlterar.getCliente().getId());
			Cliente c = clienteOpt.orElseThrow(() -> new RuntimeException(NAO_VEIO_NADA_AQUI));
						
			pedidoParaAlterar.setItens(pedido.getItens());
			double valorTotal = 0;
			for (Item i : pedido.getItens()) {
				valorTotal +=i.getPreco();
			}
			pedidoParaAlterar.setData(pedido.getData());
			pedidoParaAlterar.setValorTotal(valorTotal);			
			c.getPedidos().remove(pedidoParaAlterar);
			c.getPedidos().add(pedidoParaAlterar);
			this.clienteRepository.save(c);
		} else {
			Optional<Cliente> clienteOpt = clienteRepository.findById(pedido.getCliente().getId());
			Cliente c = clienteOpt.orElseThrow(() -> new RuntimeException(NAO_VEIO_NADA_AQUI));
			
			double valorTotal = 0;
			for (Item i : pedido.getItens()) {
				valorTotal +=i.getPreco();
			}
			pedido.setValorTotal(valorTotal);
			pedido = this.pedidoRepository.save(pedido);
			c.getPedidos().add(pedido);
			this.clienteRepository.save(c);
		}
		redirect.addFlashAttribute("globalMessage","Pedido gravado com sucesso");
		return new ModelAndView("redirect:/" + ITEM_URI + "{pedido.id}","pedido.id",pedido.getId());
	}

	@GetMapping(value = "remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id,RedirectAttributes redirect) {

		Optional<Pedido> pedidoParaRemoverOpt = pedidoRepository.findById(id);
		Pedido pedidoParaRemover = pedidoParaRemoverOpt.orElseThrow(() -> new RuntimeException("Não veio nada"));

		Optional<Cliente> clienteOpt = clienteRepository.findById(pedidoParaRemover.getCliente().getId());
		Cliente c = clienteOpt.orElseThrow(() -> new RuntimeException("Não veio nada"));
		
		c.getPedidos().remove(pedidoParaRemover);

		this.clienteRepository.save(c);
		this.pedidoRepository.deleteById(id);

		Iterable<Pedido> pedidos = this.pedidoRepository.findAll();

		ModelAndView mv = new ModelAndView(ITEM_URI + "list","pedidos",pedidos);
		mv.addObject("globalMessage","Pedido removido com sucesso");

		return mv;
	}

	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Pedido pedido) {

		Map<String,Object> model = new HashMap<>();
		model.put("todosItens",itemRepository.findAll());
		model.put("todosClientes",clienteRepository.findAll());
		model.put("pedido",pedido);

		return new ModelAndView(ITEM_URI + "form",model);
	}

}
