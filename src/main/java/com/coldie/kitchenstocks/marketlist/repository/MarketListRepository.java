package com.coldie.kitchenstocks.marketlist.repository;

import com.coldie.kitchenstocks.marketlist.model.MarketList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketListRepository extends JpaRepository<MarketList, Long> {
    Optional<MarketList> findByUserEmailEqualsAndNameEquals(String email, String name);
}
