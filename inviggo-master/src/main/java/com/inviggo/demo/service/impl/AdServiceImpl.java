package com.inviggo.demo.service.impl;

import com.inviggo.demo.dto.AdDto;
import com.inviggo.demo.mapper.AdMapper;
import com.inviggo.demo.model.Ad;
import com.inviggo.demo.model.Category;
import com.inviggo.demo.model.User;
import com.inviggo.demo.repository.AdRepository;
import com.inviggo.demo.repository.UserRepository;
import com.inviggo.demo.request.CreateAdRequest;
import com.inviggo.demo.request.UpdateAdRequest;
import com.inviggo.demo.service.AdService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.yaml.snakeyaml.tokens.Token.ID.Value;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserRepository userRepository;

    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper, UserRepository userRepository) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.userRepository = userRepository;
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
    public Page<AdDto> getAllAds(PageRequest pageRequest) {
        return adRepository.findAll(pageRequest).map(adMapper::adToAdDto);
    }

    @Override
    public AdDto getAdById(Long id){
        Optional<Ad> adOpt = adRepository.findById(id);
        return adOpt.map(adMapper::adToAdDto).orElse(null);
    }

    @Override
    public Page<AdDto> getAdsForUser(String username, int page, int size) {
        System.out.println("Username passed to getAdsForUser: " + username);
        return adRepository.findByUser_Username(username, PageRequest.of(page,size)).map(adMapper::adToAdDto);
    }

    @Override
    public Ad createAd(CreateAdRequest createAdRequest, UserDetails userDetails){
        Ad ad = new Ad();
        ad.setTitle(createAdRequest.getTitle());
        ad.setDescription(createAdRequest.getDescription());
        ad.setCity(createAdRequest.getCity());
        ad.setCategory(Category.valueOf(createAdRequest.getCategory()));
        ad.setPrice(createAdRequest.getPrice());
        ad.setImageUrl(createAdRequest.getImageUrl());

        LocalDateTime creationDate = LocalDateTime.now();
        ad.setCreatedAt(creationDate);

        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if(user != null)
            ad.setUser(user);

        adRepository.save(ad);
        return ad;
    }

    @Override
    public void deleteAdById(Long id){
        if(adRepository.existsById(id)){
            adRepository.deleteById(id);
        }else{
            throw new IllegalArgumentException("Ad with id" + id+ " doesn't exist.");
        }
    }

    @Override
    public Ad updateAd(Long id, UpdateAdRequest updateAdRequest, UserDetails userDetails) {
        Ad ad = adRepository.findById(id).orElse(null);

        if(!ad.getUser().getUsername().equals(userDetails.getUsername())){
            throw new SecurityException("Not authorized");
        }

        ad.setTitle(updateAdRequest.getTitle());
        ad.setDescription(updateAdRequest.getDescription());
        ad.setCity(updateAdRequest.getCity());
        ad.setCategory(updateAdRequest.getCategory());
        ad.setPrice(updateAdRequest.getPrice());
        ad.setImageUrl(updateAdRequest.getImageUrl());

        return adRepository.save(ad);
    }

    @Override
    public Page<AdDto> getAdsByPriceRange(Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return adRepository.findByPriceRange(minPrice, maxPrice, pageable)
                .map(adMapper::adToAdDto);
    }


    @Override
    public Page<AdDto> getFilteredAds(String category, String title, Double minPrice, Double maxPrice, Boolean showMineOnly, String username, Pageable pageable) {
        Long userId = null;
        if (showMineOnly != null && showMineOnly) {
            userId = userRepository.findByUsername(username).map(User::getId).orElse(null);
        }

        // Convert category string to Category enum
        Category categoryEnum = null;
        if (category != null) {
            try {
                categoryEnum = Category.valueOf(category); // Adjust case if necessary
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category: " + category);
            }
        }

        return adRepository.findFilteredAds(categoryEnum, title, minPrice, maxPrice, userId, pageable)
                .map(adMapper::adToAdDto);
    }
}
