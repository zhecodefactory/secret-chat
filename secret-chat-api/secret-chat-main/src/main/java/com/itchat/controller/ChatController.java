package com.itchat.controller;

import com.itchat.common.BaseInfoProperties;
import com.itchat.netty.NettyServerNode;
import com.itchat.result.GraceJSONResult;
import com.itchat.service.ChatMessageService;
import com.itchat.utils.JsonUtils;
import com.itchat.utils.PagedGridResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author 王哲
 * @Contact 1121586359@qq.com
 * @ClassName ChatController.java
 * @create 2024年09月14日 下午5:15
 * @Description 消息控制器
 * @Version V1.0
 */
@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController extends BaseInfoProperties {
    @Resource
    private ChatMessageService chatMessageService;
    @Resource(name = "curatorClient")
    private CuratorFramework zkClient;

    @PostMapping("/getMyUnReadCounts")
    public GraceJSONResult getMyUnReadCounts(String myId) {
        Map map = redis.hgetall(CHAT_MSG_LIST + ":" + myId);
        return GraceJSONResult.ok(map);
    }

    @PostMapping("/clearMyUnReadCounts")
    public GraceJSONResult clearMyUnReadCounts(String myId, String oppositeId) {
        redis.setHashValue(CHAT_MSG_LIST + ":" + myId, oppositeId, "0");
        return GraceJSONResult.ok();
    }

    @PostMapping("/list/{senderId}/{receiverId}")
    public GraceJSONResult list(@PathVariable("senderId") String senderId,
                                @PathVariable("receiverId") String receiverId,
                                Integer page,
                                Integer pageSize) {

        if (page == null) page = 1;
        if (pageSize == null) page = 20;

        PagedGridResult gridResult = chatMessageService.queryChatMsgList(
                senderId,
                receiverId,
                page,
                pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @PostMapping("/signRead/{msgId}")
    public GraceJSONResult signRead(@PathVariable("msgId") String msgId) {
        chatMessageService.updateMsgSignRead(msgId);
        return GraceJSONResult.ok();
    }

    @PostMapping("/getNettyOnlineInfo")
    public GraceJSONResult getNettyOnlineInfo() throws Exception {
        // 从zookeeper中获取当前已注册的Netty服务列表
        String path = "/server-list";
        List<String> nettyServerList = zkClient.getChildren().forPath(path);

        // 获取服务列表中的数据
        List<NettyServerNode> nettyServerNodeList = new ArrayList<>();
        for (String node : nettyServerList) {
            String nodeValue = new String(zkClient.getData().forPath(path + "/" + node));

            NettyServerNode nettyServerNode = JsonUtils.jsonToPojo(nodeValue, NettyServerNode.class);
            nettyServerNodeList.add(nettyServerNode);
        }

        // 获得哪个zk链接人数最少
        Optional<NettyServerNode> minNodeOptional = nettyServerNodeList.stream()
                .min(
                        Comparator.comparing(
                                nettyServerNode -> nettyServerNode.getOnlineCounts()
                        )
                );
        NettyServerNode minNode = minNodeOptional.get();

        return GraceJSONResult.ok(minNode);
    }

}
