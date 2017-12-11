package cn.polar.searchTest;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SearchTest {


    @Test
    public void addDocument() throws Exception {
        // 创建一个solr服务器
        SolrServer solrServer = new HttpSolrServer("http://172.16.81.129:8080/solr/collection1");
        // 创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();

        // 向document中添加域 域中的字段必须在schema.xml中存在
        document.addField("id", "hello");
        document.addField("item_title", "测试商品2");
        document.addField("item_price", "99999");


        //将文档添加到索引库中
        solrServer.add(document);
        solrServer.commit();

    }

    @Test
    public void testQuery() throws Exception {
        // 创建一个solr服务器
        SolrServer solrServer = new HttpSolrServer("http://172.16.81.129:8080/solr/collection1");
        SolrQuery query = new SolrQuery();
        query.set("q" , "*:*");
        
        QueryResponse response = solrServer.query(query);
        SolrDocumentList results = response.getResults();
        System.out.println("查询结果的总记录数:" + results.getNumFound());
        for (SolrDocument solrDocument: results
             ) {
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
            System.out.println(solrDocument.get("item_price"));
        }

    }

    @Test
    public void testComplexQuery() throws Exception {
        // 创建一个solr服务器
        SolrServer solrServer = new HttpSolrServer("http://172.16.81.129:8080/solr/collection1");
        SolrQuery query = new SolrQuery();
        query.setQuery("item_title:商品");

        query.set("df", "item_keywords");

        //高亮显示
        query.setHighlight(true);
        // 添加高亮显示的域
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");

        QueryResponse response = solrServer.query(query);
        SolrDocumentList solrDocumentList = response.getResults();
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        for (SolrDocument document: solrDocumentList
             ) {
            System.out.println(document.get("id"));
            String title = "";
            List<String> list = highlighting.get(document.get("id")).get("item_title");
            if(list != null  && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) document.get("item_title");
            }
            System.out.println(title);
            System.out.println(document.get("item_price"));


        }
    }



}
