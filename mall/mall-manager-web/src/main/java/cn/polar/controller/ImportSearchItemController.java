package cn.polar.controller;

import cn.polar.common.util.MallResult;
import cn.polar.search.service.ImportSearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImportSearchItemController {
    @Autowired
    private ImportSearchItemService importSearchItemService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public MallResult importItems() {
        MallResult result = importSearchItemService.importItems();
        return result;
    }
}
