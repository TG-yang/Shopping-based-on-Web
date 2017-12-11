package cn.polar.search.message;

import cn.polar.search.service.ImportSearchItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Description : add item index to solr
 * Created By Polar on 2017/8/16
 */
public class ItemAddMessageListener implements MessageListener{
    @Autowired
    private ImportSearchItemService importSearchItemService;
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = null;
            Long id = null;
            if(message instanceof TextMessage) {
                textMessage = (TextMessage) message;
                id = Long.parseLong(textMessage.getText());

            }
            importSearchItemService.importItemById(id);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
