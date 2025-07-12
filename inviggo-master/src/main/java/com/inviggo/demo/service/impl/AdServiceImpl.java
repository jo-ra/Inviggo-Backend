package com.inviggo.demo.service.impl;

import com.inviggo.demo.dto.AdDto;
import com.inviggo.demo.mapper.AdMapper;
import com.inviggo.demo.model.Ad;
import com.inviggo.demo.repository.AdRepository;
import com.inviggo.demo.service.AdService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;

    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
    }
//
//
//    public Page<AdTableDTO> getFilteredAds(Optional<String> category, Optional<String> title, Optional<Double> minPrice,
//                                           Optional<Double> maxPrice, Optional<Boolean> showMineOnly, Pageable pageable) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userId;
//
//        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
//            userId = showMineOnly.orElse(false) ? Long.valueOf(authentication.getName()) : null;
//        } else {
//            userId = null;
//        }
//
//        return adRepository.findFilteredAds(category, title, minPrice, maxPrice, Optional.ofNullable(userId), pageable)
//                .map(ad -> new AdTableDTO(
//                        ad.getId(),
//                        ad.getImageUrl(),
//                        ad.getTitle(),
//                        ad.getPrice(),
//                        ad.getCity(),
//                        ad.getCategory().name(),
//                        userId != null && ad.getUser().getId().equals(userId)
//                ));
//    }

    @Override
    public Page<AdDto> getAllAds(int page,int size) {
        return this.adRepository.findAll(PageRequest.of(page, size)).map(adMapper::adToAdDto);
    }

    @Override
    public AdDto getAdById(Long id){
        Optional<Ad> adOpt = adRepository.findById(id);
        return adOpt.map(adMapper::adToAdDto).orElse(null);
    }
}
