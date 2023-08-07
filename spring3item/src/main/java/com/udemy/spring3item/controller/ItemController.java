package com.udemy.spring3item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.udemy.spring3item.exception.ItemNotFoundException;
import com.udemy.spring3item.model.HelloMessage;
import com.udemy.spring3item.model.Item;
import com.udemy.spring3item.service.ItemService;

@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@GetMapping("/items")
	public List<Item> getAllItems(){
		return itemService.getAllItems();
	}
	
	@GetMapping("/items/{itemId}")
	public Item getItem(@PathVariable("itemId") Long itemId) {
		return itemService.getItem(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
	}
	
	@PostMapping("/items")
	public void addItem(@RequestBody Item item) {
		itemService.addItem(item);
	}
	
	@PutMapping("items/{itemId}")
	public void updateItem(@RequestBody Item item,
			@PathVariable("itemId") Long itemId) {
		itemService.updateItem(itemId, item);
	}
	
	@DeleteMapping("items/{itemId}")
	public void deleteItem(@PathVariable("itemId") Long itemId) {
		itemService.deleteItem(itemId);
	}
	
	@GetMapping("/callHello")
	public HelloMessage callHello() {
		return itemService.getHelloResponse();
	}
}
