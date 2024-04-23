package com.coldie.kitchenstocks.marketlist.service;

import com.coldie.kitchenstocks.item.request.ItemRequest;
import com.coldie.kitchenstocks.marketlist.model.MarketList;

public interface MarketListService {
    MarketList createMarketListItem(ItemRequest itemRequest, boolean needThrowException);
}
