package com.pu.mapper;

import com.pu.epojo.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("INSERT INTO notification (user_id, title, content, biz_type, is_read, channel, send_status, retry_count, create_time, update_time) VALUES (#{userId}, #{title}, #{content}, #{bizType}, #{isRead},#{channel}, #{sendStatus}, #{retryCount},NOW(), NOW())")
    void insert(Notification notification);

    @Update("UPDATE notification SET send_status = #{sendStatus},retry_count = #{retryCount},update_time = NOW() WHERE id = #{id}")
    void updateSendStatus(Notification notification);

    @Select("select * from notification where user_id=#{userId} order by create_time desc")
    List<Notification> getNotifyList(Long userId);

    @Update("update notification set is_read=1 where id=#{id} and user_id=#{userId}")
    int markAsRead(Long id, Long userId);

    @Select("select count(*) from notification where user_id=#{userId} and is_read=0")
    Integer countUnread(Long userId);

    @Select("select * from notification where id=#{id} and user_id=#{userId}")
    Notification getById(Long id,Long userId);
}