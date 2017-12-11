package cn.polar.search.dao;

import cn.polar.common.pojo.SearchItem;
import cn.polar.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 索引查询商品
 */
@Repository
public class SearchDao {
    @Autowired
    private SolrServer solrServer;

    public SearchResult getSeachItems(SolrQuery solrQuery) throws Exception {
        // 创建结果集
        SearchResult searchResult = new SearchResult();

        QueryResponse queryResponse = solrServer.query(solrQuery);
        // 获取结果
        SolrDocumentList solrDocumentList = queryResponse.getResults();

        long numFound = solrDocumentList.getNumFound();

        // 将总条数放入到结果集中
        searchResult.setRecordCount(numFound);
        //获取高亮内容
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        // 商品列表
        List<SearchItem> items = new ArrayList<>();
        for (SolrDocument document : solrDocumentList
                ) {
            SearchItem item = new SearchItem();
            item.setId((String) document.get("id"));
            item.setPrice((Long) document.get("item_price"));
            item.setCategory_name((String) document.get("item_category_name"));
            item.setImage((String) document.get("item_image"));
            item.setSell_point((String) document.get("item_sell_point"));
            String title = "";

            List<String> list = highlighting.get(document.get("id")).get("item_title");
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) document.get("item_title");
            }

            item.setTitle(title);
            // 将商品放入到商品列表中
            items.add(item);

        }
        // 将商品列表放入到结果集中
        searchResult.setItemList(items);
        return searchResult;
    }


}
