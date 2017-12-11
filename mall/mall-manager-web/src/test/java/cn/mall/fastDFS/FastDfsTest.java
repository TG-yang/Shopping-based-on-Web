package cn.mall.fastDFS;

import cn.polar.common.util.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

public class FastDfsTest {
    @Test
    public void testFastClientUtil() throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("/Users/Polar/IdeaProjects/mall/mall-manager-web/src/main/resources/conf/client.conf");
        String uploadFile = fastDFSClient.uploadFile("/Users/Polar/Downloads/katie_westwood_7_8_2017_23_47_9_438.jpg");
        System.out.println(uploadFile);

    }

    @Test
    public void testFastUpload () throws Exception {
        // 1、加载配置文件，配置文件中的内容就是tracker服务的地址。

        ClientGlobal.init("/Users/Polar/IdeaProjects/mall/mall-manager-web/src/main/resources/conf/client.conf");
        // 2、创建一个TrackerClient对象。直接new一个。
        TrackerClient trackerClient = new TrackerClient();
        // 3、使用TrackerClient对象创建连接，获得一个TrackerServer对象。
        TrackerServer trackerServer = trackerClient.getConnection();
        // 4、创建一个StorageServer的引用，值为null
        StorageServer storageServer = null;
        // 5、创建一个StorageClient对象，需要两个参数TrackerServer对象、StorageServer的引用
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        // 6、使用StorageClient对象上传图片。
        //扩展名不带“.”
        String[] strings = storageClient.upload_file("/Users/Polar/Documents/41bcc47fdb2a488d91740324765245ee.jpeg", "jpg", null);
        // 7、返回数组。包含组名和图片的路径。
        for (String string : strings) {
            System.out.println(string);
        }

    }
}

