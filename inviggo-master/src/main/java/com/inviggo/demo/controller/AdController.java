package com.inviggo.demo.controller;

import com.inviggo.demo.dto.AdDto;
import com.inviggo.demo.model.Ad;
import com.inviggo.demo.request.CreateAdRequest;
import com.inviggo.demo.request.UpdateAdRequest;
import com.inviggo.demo.service.AdService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.Optional;

@RestController
@RequestMapping("/ad")
public class AdController {
    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }


    @GetMapping("/getAll")
    @Operation(
            summary = "Get all ads",
            description = "Retrieve all ads with pagination and sorting. Results are sorted by creation date in descending order (newest first)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved ads",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public Page<AdDto> getAds(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "20") int size) {
        return adService.getAllAds(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get ad by ID",
            description = "Retrieve a specific advertisement by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved the ad",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ad not found",
                    content = @Content
                    )})
    public ResponseEntity<AdDto> getAdById(@PathVariable (name = "id") Long id){
        AdDto ad = adService.getAdById(id);
        return new ResponseEntity<>(ad, ad != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @Operation(
            summary = "Get ads by user",
            description = "Retrieve all advertisements posted by a specific user with pagination"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user's ads",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    @GetMapping("/user/{username}")
    public Page<AdDto> getAdsForUser(@PathVariable String username, @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size) {
        return adService.getAdsForUser(username,page, size);
    }

    @PostMapping("/create")
    @Operation(summary = "Create new ad", description = "Create a new advertisement")
    @SecurityRequirement(name = "bearerToken")
    public ResponseEntity<Boolean> createAd(@RequestBody CreateAdRequest request, @AuthenticationPrincipal UserDetails userDetails){
        Ad ad = adService.createAd(request,userDetails);
        if (ad!= null)
            return ResponseEntity.ok(true);
        return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);

    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Update ad",
            description = "Update an existing advertisement. Only the ad owner can update their ads. Requires authentication."
    )
    @SecurityRequirement(name = "bearerToken")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ad updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Only ad owner can update",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ad not found",
                    content = @Content
            )
    })
    public ResponseEntity<Ad> updateAd(@PathVariable Long id, @RequestBody UpdateAdRequest updateAdRequest, @AuthenticationPrincipal UserDetails userDetails) {
        try {
        Ad updatedAd = adService.updateAd(id,updateAdRequest,userDetails);
        return ResponseEntity.ok(updatedAd);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (SecurityException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Delete ad",
            description = "Delete an advertisement by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ad deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ad not found",
                    content = @Content
            )
    })
    public ResponseEntity<String> deleteAd(@PathVariable Long id){
        try{
            adService.deleteAdById(id);
            return ResponseEntity.ok("Ad deleted");
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/filterByPrice")
    public Page<AdDto> getAdsByPriceRange(@RequestParam(required = false) Double minPrice,
                                          @RequestParam(required = false) Double maxPrice,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        return adService.getAdsByPriceRange(minPrice, maxPrice, page, size);
    }


    @GetMapping("/filter")
    @Operation(
            summary = "Filter ads with multiple criteria",
            description = "Retrieve ads filtered by category, title, price range, and ownership. The 'showMineOnly' parameter requires authentication."
    )
    @SecurityRequirement(name = "bearerToken")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved filtered ads",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid filter parameters",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Authentication required for 'showMineOnly'",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Access denied",
                    content = @Content
            )
    })
    public Page<AdDto> getFilteredAds(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "false") Boolean showMineOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = showMineOnly ? userDetails.getUsername() : null;
        return adService.getFilteredAds(category, title, minPrice, maxPrice, showMineOnly, username, PageRequest.of(page, size));
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
