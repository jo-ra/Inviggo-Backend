package com.inviggo.demo.mapper;

import com.inviggo.demo.dto.AdDto;
import com.inviggo.demo.model.Ad;
import org.springframework.stereotype.Component;

@Component
public class AdMapper {

    public AdDto adToAdDto(Ad ad){
        if(ad==null) return null;
        AdDto adDto = new AdDto();
        adDto.setId(ad.getId());
        adDto.setCategory(ad.getCategory());
        adDto.setCity(ad.getCity());
        adDto.setPrice(ad.getPrice());
        adDto.setTitle(ad.getTitle());
        adDto.setDescription(ad.getDescription());
        adDto.setImageUrl(ad.getImageUrl());
        adDto.setCreatedAt(ad.getCreatedAt());
        adDto.setSellerName(ad.getUser().getUsername());
        adDto.setSellerPhone(ad.getUser().getPhoneNumber());
        return adDto;
    }
}

