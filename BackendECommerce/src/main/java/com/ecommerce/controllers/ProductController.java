package com.ecommerce.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class ProductController {
	@Autowired
	private ProductService service;
	
	@RequestMapping("/greet")
	public String greet() {
		return "Hello this is working ";
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts(){
		return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable int id) {
		Product product = service.getProductById(id);
		if(product != null)
			return new ResponseEntity<>(product,HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@RequestPart Product product,
										@RequestPart MultipartFile imageFile){
		try {
			Product prod = service.addProduct(product,imageFile);
			return new ResponseEntity<>(prod,HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Send the image
	@GetMapping("/product/{productId}/image")
	public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
		Product product = service.getProductById(productId);
		byte[] imageFile = product.getImageDate();
		return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);
	}
	
	//Update the Content
	@PutMapping("/product/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile){
		Product product1 = null;
		try {
			product1 = service.updateProduct(id,product,imageFile);
		}catch(Exception e) {
			return new ResponseEntity<>("Failed to Update",HttpStatus.BAD_REQUEST);
		}
		if(product1 != null)
			return new ResponseEntity<>("Updated",HttpStatus.OK);
		else
			return new ResponseEntity<>("Failed to Update", HttpStatus.BAD_REQUEST);
	}
	
	// Delete the Product
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id){
		Product product = service.getProductById(id);
		if(product != null) {
			service.deleteProduct(id);
			return new ResponseEntity<>("Deleted", HttpStatus.OK);
		}else
			return new ResponseEntity<>("Product Not Found", HttpStatus.NOT_FOUND);
			
	}
	
	//Searching 
	@GetMapping("/products/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
		List<Product> products = service.searchProducts(keyword);
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
}
