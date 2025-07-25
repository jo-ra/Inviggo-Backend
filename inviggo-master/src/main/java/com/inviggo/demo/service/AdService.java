package com.inviggo.demo.service;

import com.inviggo.demo.dto.AdDto;
import com.inviggo.demo.model.Ad;
import com.inviggo.demo.request.CreateAdRequest;
import com.inviggo.demo.request.UpdateAdRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface AdService {

    Page<AdDto> getAllAds(PageRequest pageRequest);
    AdDto getAdById(Long id);
    Page<AdDto> getAdsForUser(String username,int page,int size);
    Ad createAd(CreateAdRequest createAdRequest, UserDetails userDetails);
    void deleteAdById(Long id);
    Ad updateAd(Long id, UpdateAdRequest updateAdRequest, UserDetails userDetails);
    Page<AdDto> getAdsByPriceRange(Double minPrice, Double maxPrice, int page, int size);
    Page<AdDto> getFilteredAds(String category, String title, Double minPrice, Double maxPrice, Boolean showMineOnly, String username, Pageable pageable);

}
