package com.inviggo.demo.service;

import com.inviggo.demo.dto.AdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AdService {
//    Page<AdTableDTO> getFilteredAds(Optional<String> category, Optional<String> title, Optional<Double> minPrice,
//                                    Optional<Double> maxPrice, Optional<Boolean> showMineOnly, Pageable pageable);
    Page<AdDto> getAllAds(int page,int size);
    AdDto getAdById(Long id);
}
