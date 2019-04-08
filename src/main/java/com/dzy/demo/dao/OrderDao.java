package com.dzy.demo.dao;

import com.dzy.demo.domain.MiaoShaOrder;
import com.dzy.demo.domain.MiaoShaUser;
import com.dzy.demo.domain.OrderInfo;
import com.dzy.demo.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

    @Insert("insert order_info (user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date) values (#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate}) ")
    @SelectKey(keyColumn = "1",keyProperty = "id",resultType = long.class,before = false,statement = "SELECT LAST_INSERT_ID()")
    public long createOrder(OrderInfo orderInfo);

    @Insert("Insert miaosha_order (user_id,order_id,goods_id) values(#{userId},#{orderId},#{goodsId})")
    @SelectKey(keyColumn = "1",keyProperty = "id",resultType = long.class,before = false,statement = "SELECT LAST_INSERT_ID()")
    long createMiaoShaOrder(MiaoShaOrder order);

    @Select("select * from order_info where id=#{orderId}")
    OrderInfo getOrderByOrderId(@Param("orderId") long orderId);
}
