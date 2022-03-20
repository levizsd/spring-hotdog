
package com.dogs.greendog.playdog.controller;

import com.dogs.greendog.playdog.domain.Cliente;
import com.dogs.greendog.playdog.domain.Item;
import com.dogs.greendog.playdog.domain.Pedido;
import com.dogs.greendog.playdog.dto.PedidoDTO;
import com.dogs.greendog.playdog.repository.ClienteRepository;
import com.dogs.greendog.playdog.repository.ItemRepository;
import com.dogs.greendog.playdog.repository.PedidoRepository;
import com.dogs.greendog.playdog.service.AtualizaEstoque;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    ModelMapper modelMapper;

    public static final String POSSIVEL_PEDIDO_NULO = "Possivel pedido nulo";
    private final AtualizaEstoque atualizaEstoque;
    private final PedidoRepository pedidoRepository;
    private final ItemRepository itemRepository;
    private final ClienteRepository clienteRepository;
    private static final String ITEM_URI = "pedidos/";

    public PedidoController(PedidoRepository pedidoRepository, ItemRepository itemRepository, ClienteRepository clienteRepository, AtualizaEstoque atualizaEstoque) {
        this.pedidoRepository = pedidoRepository;
        this.itemRepository = itemRepository;
        this.clienteRepository = clienteRepository;
        this.atualizaEstoque = atualizaEstoque;
    }

    @GetMapping("/")
    public ModelAndView list() {
        Iterable<Pedido> pedidos = this.pedidoRepository.findAll();
        log.info(">>>Geting orders: {}", pedidos);
        return new ModelAndView(ITEM_URI + "list", "pedidos", pedidos);
    }

    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Pedido pedido) {
        return new ModelAndView(ITEM_URI + "view", "pedido", pedido);
    }

    @GetMapping("/novo")
    public ModelAndView createForm(@ModelAttribute PedidoDTO pedidoDTO) {

        Map<String, Object> model = new HashMap<>();
        model.put("todosItens", itemRepository.findAll());
        model.put("todosClientes", clienteRepository.findAll());
        log.info(">>>Order new: {}", model);
        return new ModelAndView(ITEM_URI + "form", model);

    }

    @PostMapping(params = "form")
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView create(@Valid PedidoDTO pedidoDTO, BindingResult result, RedirectAttributes redirect) {

        Pedido pedido = modelMapper.map(pedidoDTO, Pedido.class);


        if (result.hasErrors()) {
            return new ModelAndView(ITEM_URI + "form", "formErrors", result.getAllErrors());
        }

        if (pedido.getId() != null) {

            Optional<Pedido> pedidoParaAlterarOpt = pedidoRepository.findById(pedido.getId());
            Pedido pedidoParaAlterar = pedidoParaAlterarOpt.orElseThrow(() -> new RuntimeException(POSSIVEL_PEDIDO_NULO));

            Optional<Cliente> clienteOpt = clienteRepository.findById(pedidoParaAlterar.getCliente().getId());
            Cliente c = clienteOpt.orElseThrow(() -> new RuntimeException("Possivel cliente nulo"));
            pedidoParaAlterar.setItens(pedido.getItens());
            double valorTotal = pedido.getItens()
                    .stream().
                    mapToDouble(Item::getPreco)
                    .sum();

            pedidoParaAlterar.setData(pedido.getData());
            pedidoParaAlterar.setValorTotal(valorTotal);
            c.getPedidos().remove(pedidoParaAlterar);
            c.getPedidos().add(pedidoParaAlterar);
            log.info(">>>Create new order: {}", c);
            this.clienteRepository.save(c);
        } else {

            Optional<Cliente> clienteOpt = clienteRepository.findById(pedido.getCliente().getId());
            Cliente c = clienteOpt.orElseThrow(() -> new RuntimeException("Possivel cliente nulo"));
            double valorTotal = pedido.getItens()
                    .stream().
                    mapToDouble(Item::getPreco)
                    .sum();
            pedido.setValorTotal(valorTotal);
            pedido = this.pedidoRepository.save(pedido);
            c.getPedidos().add(pedido);
            log.info(">>>Update order: {}", c);
            this.clienteRepository.save(c);
            // atualiza estoque
            log.info(">>>Updating stock: {}", pedido);
            atualizaEstoque.processar(pedido);
        }
        redirect.addFlashAttribute("globalMessage", "Pedido gravado com sucesso");
        return new ModelAndView("redirect:/" + ITEM_URI + "{pedido.id}", "pedido.id", pedido.getId());
    }

    @GetMapping(value = "remover/{id}")
    public ModelAndView remover(@PathVariable("id") Long id, RedirectAttributes redirect) {

        Optional<Pedido> pedidoParaRemoverOpt = pedidoRepository.findById(id);
        Pedido pedidoParaRemover = pedidoParaRemoverOpt.orElseThrow(() -> new RuntimeException(POSSIVEL_PEDIDO_NULO));

        Optional<Cliente> clienteOpt = clienteRepository.findById(pedidoParaRemover.getCliente().getId());
        Cliente c = clienteOpt.orElseThrow(() -> new RuntimeException(POSSIVEL_PEDIDO_NULO));

        this.clienteRepository.save(c);
        this.pedidoRepository.deleteById(id);

        Iterable<Pedido> pedidos = this.pedidoRepository.findAll();

        ModelAndView mv = new ModelAndView(ITEM_URI + "list", "pedidos", pedidos);
        mv.addObject("globalMessage", "Pedido removido com sucesso");

        return mv;
    }

    @GetMapping(value = "alterar/{id}")
    public ModelAndView alterarForm(@PathVariable("id") Pedido pedido) {

        Map<String, Object> model = new HashMap<>();
        model.put("todosItens", itemRepository.findAll());
        model.put("todosClientes", clienteRepository.findAll());
        model.put("pedido", pedido);

        return new ModelAndView(ITEM_URI + "form", model);
    }

}
