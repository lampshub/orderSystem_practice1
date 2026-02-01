package com.beyond23.orderSystem.product.controller;

import com.beyond23.orderSystem.common.dtos.CommonErrorDto;
import com.beyond23.orderSystem.product.dtos.ProductCreateDto;
import com.beyond23.orderSystem.product.dtos.ProductResDto;
import com.beyond23.orderSystem.product.dtos.ProductSearchDto;
import com.beyond23.orderSystem.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    create, detail/1, list

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@ModelAttribute ProductCreateDto dto){
        Long productId = productService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }


    @GetMapping("/list")
    public ResponseEntity<?> findAll(Pageable pageable,@ModelAttribute ProductSearchDto searchDto){
        Page<ProductResDto> productResDtoList = productService.findAll(pageable, searchDto);
        return ResponseEntity.status(HttpStatus.OK).body(productResDtoList);
    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
//        try {
            ProductResDto dto = productService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
//        } catch (NoSuchElementException e) {
//            CommonErrorDto dto = CommonErrorDto.builder()
//                    .status_code(404)
//                    .error_message(e.getMessage())
//                    .build();
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
//        }
    }



//    public Long create(ProductCreateDto dto, @RequestParam(value = "profileImage") MultipartFile profileImage){
//        return productService.save(dto,profileImage);     //이렇게 해도 id값으로 사용자한테 리턴 ?
//    }
//
//    @GetMapping("/product/list")
//    public Page<ProductListDto> findByAll(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, @ModelAttribute ProductSearchDto searchDto){
//        Page<ProductListDto> dto = productService.findByAll(pageable, searchDto);
//
//        return dto;
//    }
//
//    @GetMapping("/product/{id}")
//    public ProductDetailDto findById(@PathVariable Long id){
//        ProductDetailDto dto = productService.findById(id);
//
//        return dto;
//    }
}
