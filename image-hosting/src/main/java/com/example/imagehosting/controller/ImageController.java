package com.example.imagehosting.controller;

import com.example.imagehosting.model.Item;
import com.example.imagehosting.service.FileService;
import com.example.imagehosting.service.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private FileService fileService;

    @GetMapping
    public String viewHomePage(Model model) {
        model.addAttribute("images", imageService.getAllItems());
        return "index";
    }

    @Value("${upload.path}")
    private String uploadDir;

    @PostMapping("/images/add")
    public String addFile(@RequestParam("file") MultipartFile file,
                          @RequestParam("description") String description,
                          @RequestParam("name") String name) throws IOException {
        if (file.isEmpty()) {
            return "redirect:/images?error=emptyfile";
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setLink(filename); // <имя файла>
        imageService.saveItem(item);

        return "redirect:/#gallery-headline";
    }


    @GetMapping("/images/edit/{id}")
    public String editItem(@PathVariable("id") Long id, Model model) {
        Item item = imageService.getAllItems().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + id));

        model.addAttribute("item", item);
        return "edit-item";
    }

    @PostMapping("/images/edit/{id}")
    public String updateItem(@PathVariable Long id, Item item) {
        imageService.saveItem(item);
        return "redirect:/#gallery-headline";
    }

    @PostMapping("/images/delete/{id}")
    public String deleteItem(@PathVariable("id") Long id) {
        // Получаем элемент из базы данных
        Item item = imageService.getAllItems().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + id));

        // Извлечение имени файла из ссылки на изображение
//        String filename = item.getLink().substring(item.getLink().lastIndexOf("/") + 1);

        String filename = Paths.get(item.getLink()).getFileName().toString();

        // Удаление файла с использованием FileService
        boolean fileDeleted = fileService.deleteFile(filename);
        if (!fileDeleted) {
            return "redirect:/images?error=deletefile";
        }

        // Удаление записи из базы данных
        imageService.deleteItem(id);
        return "redirect:/#gallery-headline";
    }
}


