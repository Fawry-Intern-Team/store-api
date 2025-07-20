package com.example.store_service.mapper;

import com.example.store_service.dto.StockDto;
import com.example.store_service.model.Stock;
import com.example.store_service.model.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    //@Mapping(source = "storeId", target = "store", qualifiedByName = "mapStoreIdToStore")
    Stock toEntity(StockDto dto);

    @Named("mapStoreIdToStore")
    static Store mapStoreIdToStore(UUID storeId) {
        if (storeId == null) return null;
        Store store = new Store();
        store.setId(storeId);
        return store;
    }
}