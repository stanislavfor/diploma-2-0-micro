package com.example.imagehosting.repository;

import com.example.imagehosting.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}

