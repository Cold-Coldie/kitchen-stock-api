package com.coldie.kitchenstocks.item.respository;

import com.coldie.kitchenstocks.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByUser_EmailEquals(String email, Pageable pageable);

    Optional<Item> findByUser_EmailEqualsAndIdEquals(String email, Long id);

    Page<Item> findAllByUser_EmailEqualsAndNameContaining(String email, String name, Pageable pageable);

    Optional<Item> findByUser_EmailEqualsAndNameEquals(String email, String name);

    List<Item> findAllByUser_EmailEqualsAndMeasuringUnit_IdEquals(String email, Long id);

    void deleteAllByUser_EmailEqualsAndMeasuringUnit_IdEquals(String email, Long id);
}
