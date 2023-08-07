package com.udemy.spring3item.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.udemy.spring3item.model.HelloMessage;
import com.udemy.spring3item.model.Item;
import com.udemy.spring3item.repo.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	private RestTemplate restTemplate;
	
	public ItemService (RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	
	/*
	private List<Item> allItems = new ArrayList<>(Arrays.asList(
			new Item("10001", "ネックレス", "ジュエリ"),
			new Item("10002", "パーカー", "ファッション"),
			new Item("10003", "ファイスクリーム", "ビューティ"),
			new Item("10004", "サプリメント", "ヘルス"),
			new Item("10005", "ブルーベリー", "フード")
			));
	*/
	
	@Cacheable("getItems")
	public List<Item> getAllItems() {
		List<Item> allItems = new ArrayList<>();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO handle exception
			e.printStackTrace();
		}
		itemRepository.findAll().forEach(allItems::add);
		
		return allItems;
	}
	
	@Cacheable(value="getItem", key="#p0")
	public Optional<Item> getItem(Long itemId) {
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO handle exception
			e.printStackTrace();
		}
		return itemRepository.findById(itemId);
	}
	
	@CacheEvict(value="getItems", allEntries=true)
	public void addItem(Item item) {
		itemRepository.save(item);
	}
	
	@Caching(evict = {
			@CacheEvict(value="getItem", key="#p0"),
			@CacheEvict(value="getItems", allEntries=true)
	})
	public void updateItem(Long itemId, Item item) {
		if(itemRepository.findById(itemId).get() != null) {
			itemRepository.save(item);
		}
	}

	@Caching(evict = {
			@CacheEvict(value="getItem", key="#p0"),
			@CacheEvict(value="getItems", allEntries=true)
	})
	public void deleteItem(Long itemId) {
		itemRepository.deleteById(itemId);
	}
	
	public HelloMessage getHelloResponse() {
		String URL = "http://localhost:8081/hello";
		String hello = restTemplate.getForObject(URL, String.class);
		
		HelloMessage retHello = new HelloMessage(hello);
		
		return retHello;
	}
}
