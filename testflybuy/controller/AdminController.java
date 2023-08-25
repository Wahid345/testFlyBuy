package com.example.testflybuy.controller;

import com.example.testflybuy.model.Category;
import com.example.testflybuy.model.Product;
import com.example.testflybuy.modelDto.ProductDto;
import com.example.testflybuy.service.CategoryService;
import com.example.testflybuy.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class AdminController {
    public static String uploadDir=System.getProperty("user.dir")+ "src/main/resources/static/productImages";

    //Category Section
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }
    @GetMapping("/admin/categories")
    public String getCat(Model model){
        model.addAttribute("categories",categoryService.getAllCategory());
        return "categories";
    }
    @GetMapping("/admin/categories/add")
    public String getCatAdd(Model model){
        model.addAttribute("category",new Category());
        return "categoriesAdd";
    }
    @PostMapping("/admin/categories/add")
    public String postCatAdd(@ModelAttribute("category") Category category){
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCat(@PathVariable long id){
        categoryService.removeCategoryById(id);

        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCat(@PathVariable long id,Model model){
        Optional<Category> category=categoryService.getCategoryById(id);

        if(category.isPresent()){
            model.addAttribute("category",category.get());
            return "categoriesAdd";
        }else{
            return "404";
        }
    }
    //Product Section
    @GetMapping("/admin/products")
    public String products(Model model){
       model.addAttribute("products",productService.getAllProduct());
       return "products";
    }
    @GetMapping("/admin/products/add")
    public String productsAddGet(Model model){
        model.addAttribute("productDTO",new ProductDto());
        model.addAttribute("categories",categoryService.getAllCategory());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String productAddPost(@ModelAttribute("productDTO")ProductDto productDto,
                                 @RequestParam("productImage")MultipartFile file,
                                 @RequestParam("imgName")String imgName)throws IOException {
        Product product=new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCategory(categoryService.getCategoryById(productDto.getCategoryId()).get());
        product.setWeight(productDto.getWeight());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        String imageUUID;
        if(!file.isEmpty()){
            imageUUID= file.getOriginalFilename();
            Path fileNameAndPath= Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath,file.getBytes());

        }else{
            imageUUID=imgName;

        }
        product.setImageName(imageUUID);
        productService.addProduct(product);

        return "redirect:/admin/products";

    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id){
        productService.removeProductById(id);

        return "redirect:/admin/products";
    }
    @GetMapping("/admin/product/update/{id}")
    public String updateProductGet(@PathVariable long id, Model model){
        Product product=productService.getProductById(id).get();
        ProductDto productDto=new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setWeight(product.getWeight());
        productDto.setCategoryId((product.getCategory().getId()));
        productDto.setImageName(product.getImageName());

        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("productDto",productDto);

        return "productsAdd";
    }


}















