package cn.polar.search.service.impl;

import cn.polar.common.pojo.SearchItem;
import cn.polar.common.util.MallResult;
import cn.polar.search.mapper.ItemSearchMapper;
import cn.polar.search.service.ImportSearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportSearchItemServiceImpl implements ImportSearchItemService {
    @Autowired
    private ItemSearchMapper itemSearchMapper;

    @Autowired
    private SolrServer solrServer;

    /**
     * 商品导入索引库服务
     *
     * @return
     */
    @Override
    public MallResult importItems() {
        try {
            List<SearchItem> itemList = itemSearchMapper.getItemList();
            for (SearchItem item : itemList
                    ) {
                SolrInputDocument document = new SolrInputDocument();

                // 向文档中添加域
                document.addField("id", item.getId());
                document.addField("item_title", item.getTitle());
                document.addField("item_sell_point", item.getSell_point());
                document.addField("item_price", item.getPrice());
                document.addField("item_image", item.getImage());
                document.addField("item_category_name", item.getCategory_name());

                // 写入索引库
                solrServer.add(document);
            }
            //提交
            solrServer.commit();
            return MallResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return MallResult.build(500, "导入数据失败");

        }

    }

    @Override
    public void importItemById(Long id) throws Exception {
        // 防止消息传过来时 数据库添加数据操作未完成，导致取出的item为空
        Thread.sleep(100);
        SearchItem item = itemSearchMapper.getItemById(id);
        if (item != null) {

            SolrInputDocument document = new SolrInputDocument();

            // 向文档中添加域
            document.addField("id", item.getId());
            document.addField("item_title", item.getTitle());
            document.addField("item_sell_point", item.getSell_point());
            document.addField("item_price", item.getPrice());
            document.addField("item_image", item.getImage());
            document.addField("item_category_name", item.getCategory_name());

            // 写入索引库
            solrServer.add(document);
            solrServer.commit();
        }


    }
}
