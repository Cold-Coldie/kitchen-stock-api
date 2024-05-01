package com.coldie.kitchenstocks.item.respository;

import com.coldie.kitchenstocks.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByUserEmailEquals(String email, Pageable pageable);

    Optional<Item> findByUserEmailEqualsAndIdEquals(String email, Long id);

    Page<Item> findAllByUserEmailEqualsAndNameContaining(String email, String name, Pageable pageable);

    Optional<Item> findByUserEmailEqualsAndNameEquals(String email, String name);
}
