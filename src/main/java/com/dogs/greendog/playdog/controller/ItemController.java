
package com.dogs.greendog.playdog.controller;

import com.dogs.greendog.playdog.domain.Item;
import com.dogs.greendog.playdog.repository.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/itens")
public class ItemController {

	private final ItemRepository itemRepository;
	private static final String ITEM_URI = "itens/";

	public ItemController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@GetMapping("/")
	public ModelAndView list() {
		Iterable<Item> itens = this.itemRepository.findAll();
		return new ModelAndView(ITEM_URI + "list","itens",itens);
	}

	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Item item) {
		return new ModelAndView(ITEM_URI + "view","item",item);
	}

	@GetMapping("/novo")
	public String createForm(@ModelAttribute Item item) {
		return ITEM_URI + "form";
	}

	@PostMapping(params = "form")
	public ModelAndView create(@Valid Item item,BindingResult result,RedirectAttributes redirect) {

		if (result.hasErrors()) {
			return new ModelAndView(ITEM_URI + "form","formErrors",result.getAllErrors());
		}
		item = this.itemRepository.save(item);
		redirect.addFlashAttribute("globalMessage","Item gravado com sucesso");
		return new ModelAndView("redirect:/" + ITEM_URI + "{item.id}","item.id",item.getId());
	}

	@GetMapping(value = "remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id,RedirectAttributes redirect) {
		this.itemRepository.deleteById(id);
		Iterable<Item> itens = this.itemRepository.findAll();
		
		ModelAndView mv = new ModelAndView(ITEM_URI + "list","itens",itens);
		mv.addObject("globalMessage","Item removido com sucesso");
	
		return mv;
	}

	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Item item) {
		return new ModelAndView(ITEM_URI + "form","item",item);
	}

}
