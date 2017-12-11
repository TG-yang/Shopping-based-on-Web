package cn.polar.search.service.impl;

import cn.polar.common.pojo.SearchResult;
import cn.polar.search.dao.SearchDao;
import cn.polar.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    /**
     * 获取商品搜索结果集
     * @param keyWord 搜索关键字
     * @param page 当前页
     * @param rows 每页记录数
     * @return
     * @throws Exception
     */
    @Override
    public SearchResult getSearchItems(String keyWord, Integer page, Integer rows) throws Exception {
        // 创建查询条件
        SolrQuery solrQuery = new SolrQuery();
        // 设置查询条件
        solrQuery.setQuery(keyWord);
        // 设置默认搜索域
        solrQuery.set("df", "item_keywords");

        // 设置分页条件
        solrQuery.setStart((page - 1) * rows);
        solrQuery.setRows(rows);

        // 高亮查询开启
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
        solrQuery.setHighlightSimplePost("</em>");

        SearchResult searchResult = searchDao.getSeachItems(solrQuery);
        // 设置总页数 取上整
        searchResult.setTotalPages((int) Math.ceil(searchResult.getRecordCount() * 1.0 / rows));


        return searchResult;
    }


}

