package dev.aco.back.Controller;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dev.aco.back.DTO.Article.LikeDTO;
import dev.aco.back.Entity.User.Member;
import dev.aco.back.service.ArticleService.ArticleLikeService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Log4j2

public class LikeController {
    private final ArticleLikeService service;

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public ResponseEntity<Boolean> likeRequest(@RequestBody LikeDTO dto){
        log.info(dto.getLikeId());
        return new ResponseEntity<Boolean>(service.likeUser(dto), HttpStatus.OK);
    }

    // @RequestMapping(value = "/like", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, 
    //     produces = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<HashMap<String, Object>> register(@RequestBody Member memberId) {
    //     HashMap<String, Object> result = service.getListArticleId(memberId.getMemberId());
    //     log.info(memberId.getMemberId());
    //     return new ResponseEntity<>(result, HttpStatus.OK);
    // }
    
}