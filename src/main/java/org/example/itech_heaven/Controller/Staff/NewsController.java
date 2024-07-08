package org.example.itech_heaven.Controller.Staff;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.DTO.NewsUpdateDTO;
import org.example.itech_heaven.Entity.News;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.NewsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/manage-news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private static final String contentDir = "src/main/resources/static/content/";
    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/uploaded/";


    @GetMapping("")
    public String News(Model model){

        List<News> newsList = newsService.getAllNews();

        model.addAttribute("newsList", newsList);
        return "manage-news";
    }

    @GetMapping("/update")
    public String updateNews(Model model,
                       @RequestParam(name = "id", defaultValue = "0")int id){
        NewsUpdateDTO newsUpdateDTO = new NewsUpdateDTO();
        if (id != 0){
            News news = newsService.findById(id);
            String contentFilePath = news.getContent();
            String filePath = contentFilePath.replace("\\", File.separator);
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));

                newsUpdateDTO.setId(news.getId());
                newsUpdateDTO.setTitle(news.getTitle());
                newsUpdateDTO.setImage(news.getImage());
                newsUpdateDTO.setContent(content);

            } catch (IOException e) {
                e.printStackTrace();

            }



        }
        model.addAttribute("newsUpdate", newsUpdateDTO);
        return "form-update-news";
    }


    @GetMapping("/delete")
    public String deleteNews(@RequestParam("id")int id) throws IOException {
        News news = newsService.findById(id);
        if (news != null){
            String contentFilePath = news.getContent();
            Path path_content = Paths.get(contentFilePath);
            Files.deleteIfExists(path_content);

            String imageFileName = news.getImage();

            Path path_iamge = Paths.get("src/main/resources/static" + imageFileName);
            Files.deleteIfExists(path_iamge);
            newsService.deleteNews(id);
        }


        return "redirect:/manage-news";
    }


    @PostMapping("/save")
    public String create(@RequestParam("title")String title,
                         @RequestParam(name = "news-id", defaultValue = "0")int id,
                         @RequestParam("content")String content,
                         @RequestParam("image") MultipartFile image
                         ) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        News news = new News();
        if (id != 0){
            news = newsService.findById(id);
            if (!title.equals(news.getTitle())){
                String contentFilePath = news.getContent();
                Path path = Paths.get(contentFilePath);
                Files.deleteIfExists(path);
            }
        }
        news.setAuthor(user.getFirstname() +" " + user.getLastname());
        news.setTitle(title);
        String contentFileName = title.replaceAll("[\\s/]+", "_") + ".html";
        Path contentFilePath = Paths.get(contentDir, contentFileName);
        Files.write(contentFilePath, content.getBytes());
        news.setContent(contentFilePath.toString());
        news.setDate(LocalDateTime.now());

        if (!image.isEmpty()) {
            try {
                String originalFilename = image.getOriginalFilename().toLowerCase();
                if (originalFilename.endsWith(".jpg") || originalFilename.endsWith(".jpeg") || originalFilename.endsWith(".png")) {
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String randomFilename = UUID.randomUUID() + fileExtension;

                    byte[] bytes = image.getBytes();
                    Path path = Paths.get(UPLOADED_FOLDER + randomFilename);
                    Files.write(path, bytes);
                    news.setImage("/images/uploaded/" + randomFilename);
                }
                else {
                    return "redirect:/manage-news?image-error";
                }
            } catch (IOException e) {
                return "redirect:/manage-news?image-error";
            }
        }


        newsService.saveNews(news);

        return "redirect:/manage-news";
    }



}
