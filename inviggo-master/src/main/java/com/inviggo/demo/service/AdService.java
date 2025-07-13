package com.inviggo.demo.service;

import com.inviggo.demo.dto.AdDto;
import com.inviggo.demo.model.Ad;
import com.inviggo.demo.request.CreateAdRequest;
import com.inviggo.demo.request.UpdateAdRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface AdService {
//    Page<AdTableDTO> getFilteredAds(Optional<String> category, Optional<String> title, Optional<Double> minPrice,
//                                    Optional<Double> maxPrice, Optional<Boolean> showMineOnly, Pageable pageable);
    Page<AdDto> getAllAds(int page,int size);
    AdDto getAdById(Long id);
    Page<AdDto> getAdsForUser(String username,int page,int size);
    Ad createAd(CreateAdRequest createAdRequest, UserDetails userDetails);
    void deleteAdById(Long id);
    Ad updateAd(Long id, UpdateAdRequest updateAdRequest, UserDetails userDetails);
    Page<AdDto> getAdsByPriceRange(Double minPrice, Double maxPrice, int page, int size);

}
