package com.nuradha.hello.controller;

import com.nuradha.hello.model.FoodPost;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    private static final List<FoodPost> posts = new ArrayList<>();
    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/food-post")
    public String foodPostForm(@ModelAttribute("foodPost") FoodPost foodPost) {
        return "food-post-form";
    }

    @PostMapping("/food-post")
    public String foodPostSubmit(@ModelAttribute("foodPost") FoodPost foodPost,
                                 @RequestParam("images") MultipartFile[] images,
                                 RedirectAttributes redirectAttributes) throws IOException {
        if (images.length < 1 || images.length > 3) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please upload 1–3 images.");
            return "redirect:/food-post";
        }

        // Generate ID and save images
        foodPost.setId((long) (posts.size() + 1));
        String[] imagePaths = new String[images.length];
        for (int i = 0; i < images.length; i++) {
            String fileName = UUID.randomUUID() + "_" + images[i].getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, images[i].getBytes());
            imagePaths[i] = fileName;
        }
        foodPost.setImagePaths(imagePaths);

        // Save post
        posts.add(foodPost);

        // Add success message
        redirectAttributes.addFlashAttribute("successMessage", "Post Created Successfully");

        return "redirect:/food-result";
    }

    @GetMapping("/food-result")
    public String foodPostResult(Model model) {
        model.addAttribute("posts", posts);
        return "food-post-result";
    }

    @GetMapping("/food-post/update/{id}")
    public String updatePostForm(@PathVariable("id") Long id, Model model) {
        FoodPost post = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (post == null) {
            return "redirect:/food-post";
        }
        model.addAttribute("foodPost", post);
        return "food-post-form";
    }

    @PostMapping("/food-post/update/{id}")
    public String updatePostSubmit(@PathVariable("id") Long id,
                                   @ModelAttribute("foodPost") FoodPost updatedPost,
                                   @RequestParam("images") MultipartFile[] images,
                                   RedirectAttributes redirectAttributes) throws IOException {
        FoodPost post = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (post == null) {
            return "redirect:/food-post";
        }

        // Update fields
        post.setTitle(updatedPost.getTitle());
        post.setDescription(updatedPost.getDescription());
        if (images != null && images.length > 0 && images.length <= 3) {
            String[] imagePaths = new String[images.length];
            for (int i = 0; i < images.length; i++) {
                String fileName = UUID.randomUUID() + "_" + images[i].getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR, fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, images[i].getBytes());
                imagePaths[i] = fileName;
            }
            post.setImagePaths(imagePaths);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Post Updated Successfully");
        return "redirect:/food-result";
    }

    @PostMapping("/food-post/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        posts.removeIf(p -> p.getId().equals(id));
        return "redirect:/food-result";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}