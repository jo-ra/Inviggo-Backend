package com.inviggo.demo.controller;

import com.inviggo.demo.dto.AdDto;
import com.inviggo.demo.model.Ad;
import com.inviggo.demo.request.CreateAdRequest;
import com.inviggo.demo.service.AdService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/ad")
public class AdController {
    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("/getAll")
    public Page<AdDto> getAds(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size) {
        return adService.getAllAds(page,size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdDto> getAdById(@PathVariable (name = "id") Long id){
        AdDto ad = adService.getAdById(id);
        return new ResponseEntity<>(ad, ad != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<Boolean> createAd(@RequestBody CreateAdRequest request, @AuthenticationPrincipal UserDetails userDetails){
        Ad ad = adService.createAd(request,userDetails);
        if (ad!= null)
            return ResponseEntity.ok(true);
        return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);

    }
    
}

//
//
//    @GetMapping("/api/ads")
//    public Page<AdTableDTO> getAds(
//            @RequestParam Optional<String> category,
//            @RequestParam Optional<String> title,
//            @RequestParam Optional<Double> minPrice,
//            @RequestParam Optional<Double> maxPrice,
//            @RequestParam Optional<Boolean> showMineOnly,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size
//    ) {
//        return adService.getFilteredAds(category, title, minPrice, maxPrice, showMineOnly, PageRequest.of(page, size));
//    }
//}
