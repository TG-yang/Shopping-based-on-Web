package cn.polar.controller;

import cn.polar.common.util.FastDFSClient;
import cn.polar.common.util.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {
    // 使用$取出内容
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    /**
     * 上传图片 返回值为String类型的json字符串，兼容多浏览器
     *
     * @param uploadFile
     * @return kindEdit上传图片所需的返回信息
     */
    @RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    @ResponseBody
    public String fileUpload(MultipartFile uploadFile) {
        try {
            // 获取文件扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            // 创建FastDfs客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:/conf/client.conf");
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);

            // 拼接返回的url
            url = IMAGE_SERVER_URL + url;

            // 返回成功信息
            Map<String, Object> result = new HashMap<>();
            result.put("error", 0);
            result.put("url", url);
            return JsonUtils.objectToJson(result);

        } catch (Exception e) {
            // 返回成功信息
            Map<String, Object> map = new HashMap<>();
            map.put("error", 1);
            map.put("message", "图片上传失败");
            String s = JsonUtils.objectToJson(map);
            return s;

        }
    }


}
