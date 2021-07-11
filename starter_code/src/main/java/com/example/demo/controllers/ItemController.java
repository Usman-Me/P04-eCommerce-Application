package com.example.demo.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	private Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		log.info("[Info] Order: ", itemRepository.findAll().size());
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		log.info("[Info] FindbyId: ", itemRepository.findById(id));
		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		log.info("Items:", items == null || items.isEmpty() ? "Not found" : "Items found");
		return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
				: ResponseEntity.ok(items);
			
	}
	
	
}
