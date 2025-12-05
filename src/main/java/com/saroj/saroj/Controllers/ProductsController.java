package com.saroj.saroj.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.saroj.saroj.models.product;
import com.saroj.saroj.services.ProductsRepository;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository repo;

    @GetMapping({ "", "/" })
    public String showProductList(Model model) {
        List<product> listProducts = repo.findAll();
        model.addAttribute("products", listProducts);
        return "products/index";
    }
}
