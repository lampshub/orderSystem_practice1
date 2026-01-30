package com.beyond23.orderSystem.product.service;

import com.beyond23.orderSystem.member.domain.Member;
import com.beyond23.orderSystem.member.repository.MemberRepository;
import com.beyond23.orderSystem.product.domains.Product;
import com.beyond23.orderSystem.product.dtos.ProductCreateDto;
import com.beyond23.orderSystem.product.dtos.ProductDetailDto;
import com.beyond23.orderSystem.product.dtos.ProductListDto;
import com.beyond23.orderSystem.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
//    private final S3Client = s3Client;
    @Autowired
    public ProductService(ProductRepository productRepository, MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public Long save(ProductCreateDto dto){
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("찾는 이메일이 없습니다"));
        Product product = dto.toEntity(member);
        productRepository.save(product);
            return product.getId();
    }

    public ProductListDto detail(Long id){
        Optional<Product> optProduct = productRepository.findById(id);
        Product product = optProduct.orElseThrow(()->new NoSuchElementException("찾는 상품의id가 존재하지 않습니다."));
        ProductListDto dto = ProductListDto.fromEntity(product);
        return dto;
    }
//    public Long save(ProductCreateDto dto, MultipartFile profileImage) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
//
//        Member member = memberRepository.findAllByEmail(email).orElseThrow(() -> new EntityNotFoundException("이메일이 없습니다"));
//
//        Product product = dto.toEntity(member);
//        productRepository.save(product);
////        if (profileImage != null) {
////            String fileName = "product-" + product.getId() + "-profileimage-" + profileImage.getOriginalFilename();
////            PutObjectRequest request = PutObjectRequest.builder()
////                    .bucket(bucket)
////                    .key(fileName)
////                    .contentType(profileImage.getContentType())
////                    .build();
////            try {
////                s3Client.putObject(request, RequestBody.fromBytes(profileImage.getBytes()));
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            }
////            String imgUrl = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();
////            product.updatePorfileImageUrl(imgUrl);
////        }
//        return product.getId();
//    }
//    public Page<ProductListDto> findByAll(Pageable pageable, ProductSearchDto searchDto){
//
//        Specification<Product> specification = new Specification<Product>() {
//            @Override
//            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicateList = new ArrayList<>();
//                if (searchDto.getProductName() != null) {
//                    predicateList.add(criteriaBuilder.like(root.get("productName"), "%" + searchDto.getProductName() + "%"));
//                }
//                if (searchDto.getCategory() != null) {
//                    predicateList.add(criteriaBuilder.equal(root.get("category"), searchDto.getCategory()));
//                }
//                Predicate[] predicatesArr = new Predicate[predicateList.size()];
//                for (int i = 0; i < predicatesArr.length; i++) {
//                    predicatesArr[i] = predicateList.get(i);
//                }
//                Predicate predicate = criteriaBuilder.and(predicatesArr);
//
//                return predicate;
//            }
//        };
//        Page<Product> products = productRepository.findAll(specification, pageable);
//        Page<ProductListDto> dto = products.map(p->ProductListDto.fromEntity(p));
//        return dto;
//    }
//
//    public ProductDetailDto findById(Long id){
//        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("상품이 없습니다"));
//        ProductDetailDto dto = ProductDetailDto.fromEntity(product);
//
//        return dto;
//    }
}
