package com.itchat.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itchat.bo.NewFriendsBO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 好友请求记录表 拓展 Mapper 接口
 * </p>
 *
 * @author 王青玄
 * @since 2024-09-07
 */
public interface FriendRequestMapperCustom {

    Page<NewFriendsBO> queryNewFriendList(
            @Param("page") Page<NewFriendsBO> pageInfo,
            @Param("paramMap") Map<String, Object> map);
}
