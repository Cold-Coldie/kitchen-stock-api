package com.coldie.kitchenstocks.item.respository;

import com.coldie.kitchenstocks.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUserId(Long userId);

    Optional<Item> findByIdAndUserId(Long itemId, Long userId);

    List<Item> findByNameContainingAndUserId(String name, Long userId);

    Optional<Item> findByNameEqualsAndUserId(String name, Long userId);
}
