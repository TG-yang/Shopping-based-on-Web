package cn.polar.search.controller;

import cn.polar.common.pojo.SearchResult;
import cn.polar.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Value("${PAGE_ROWS}")
    private Integer PAGE_ROWS;

    @RequestMapping("/search")
    public String getSearchItems(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws Exception {

        keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
        SearchResult result = searchService.getSearchItems(keyword, page, PAGE_ROWS);

        model.addAttribute("query", keyword);
        model.addAttribute("itemList", result.getItemList());
        model.addAttribute("recordCount", result.getRecordCount());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", result.getTotalPages());
//        System.err.println(result.getItemList().get(0).getImages());


        return "search";
    }
}
