package com.mendittzo.elastic.query.controller;

import com.mendittzo.elastic.query.dto.ElasticDTO;
import com.mendittzo.elastic.query.service.ElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/elastic")
@RequiredArgsConstructor
public class ElasticController {

    private final ElasticService elasticService;

    // GET 요청을 통해 자동완성 및 부분 검색 기능을 제공하는 엔드포인트
    @GetMapping("/autocomplete")
    public List<ElasticDTO> autocomplete(@RequestParam String query) {
        return elasticService.searchByTitle(query); // Service의 메서드를 호출하여 검색 결과를 반환
    }
}
