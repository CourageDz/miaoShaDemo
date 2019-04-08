package com.dzy.demo.dao;

import com.dzy.demo.domain.MiaoShaOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Mapper
public interface MiaoShaDao {

    @Select("select * from miaosha_order where goods_id =#{goodsId} and user_id=#{userId}")
    public MiaoShaOrder getMiaoShaOrderByUserIdGoodsId(@Param("goodsId") long goodsId,@Param("userId") Long id);
}
