package com.saroj.saroj.Controllers;

import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.saroj.saroj.models.ProductDTO;
import com.saroj.saroj.models.product;
import com.saroj.saroj.services.ProductsRepository;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository repo;

    @GetMapping({ "", "/" })
    public String showProductList(Model model) {
        List<product> listProducts = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", listProducts);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        var productsDto = new ProductDTO();
        model.addAttribute("productDto", productsDto);
        return "products/createProdcut";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("productDto") ProductDTO productDto, BindingResult result,
            Model model) {

        if (productDto.getImage() == null || productDto.getImage().isEmpty()) {
            result.addError(new FieldError("productDto", "image", "The image file is required"));
        }

        if (result.hasErrors()) {
            model.addAttribute("productDto", productDto);
            return "products/createProdcut";
        }

        MultipartFile image = productDto.getImage();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            System.out.println("Could not save image file: " + e.getMessage());
        }
        product prd = new product();
        prd.setName(productDto.getName());
        prd.setBrand(productDto.getBrandName());
        prd.setCategory(productDto.getCategoryName());
        prd.setPrice(productDto.getPrice());
        prd.setDescription(productDto.getDescription());
        prd.setCreatedAt(createdAt);
        prd.setImageFileName(storageFileName);

        repo.save(prd);

        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam("id") Integer id) {
        try {
            product prd = repo.findById(id).get();
            model.addAttribute("product", prd);

            ProductDTO prdDto = new ProductDTO();
            prdDto.setName(prd.getName());
            prdDto.setBrandName(prd.getBrand());
            prdDto.setCategoryName(prd.getCategory());
            prdDto.setPrice(prd.getPrice());
            prdDto.setDescription(prd.getDescription());
            model.addAttribute("productDto", prdDto);
        } catch (Exception e) {
            System.out.println("Error fetching product: " + e.getMessage());
        }
        return "products/editProduct";
    }

    @PostMapping("/edit")
    public String editProduct(@Valid @ModelAttribute("productDto") ProductDTO productDto, BindingResult result,
            Model model, @RequestParam("id") Integer id) {
        try {
            product existingProduct = repo.findById(id).get();
            model.addAttribute("product", existingProduct);

            if (result.hasErrors()) {
                return "products/editProduct";
            }

            if (!productDto.getImage().isEmpty()) {
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + existingProduct.getImageFileName());
                try {
                    Files.delete(oldImagePath);
                } catch (Exception e) {
                    System.out.println("Could not delete old image file: " + e.getMessage());
                }

                MultipartFile image = productDto.getImage();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                existingProduct.setImageFileName(storageFileName);
            }

            existingProduct.setName(productDto.getName());
            existingProduct.setBrand(productDto.getBrandName());
            existingProduct.setCategory(productDto.getCategoryName());
            existingProduct.setPrice(productDto.getPrice());
            existingProduct.setDescription(productDto.getDescription());
            repo.save(existingProduct);

        } catch (Exception e) {
            System.out.println("Could not save image file: " + e.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam("id") Integer id) {
        try {
            product extPrd = repo.findById(id).get();
            String uploadDir = "public/images/";
            Path imagePath = Paths.get(uploadDir + extPrd.getImageFileName());
            try {
                Files.delete(imagePath);
            } catch (Exception e) {
                System.out.println("Could not delete image file: " + e.getMessage());
            }
            repo.delete(extPrd);
        } catch (Exception e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }

}
