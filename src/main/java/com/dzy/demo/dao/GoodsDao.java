package com.dzy.demo.dao;

import com.dzy.demo.domain.Goods;
import com.dzy.demo.domain.MiaoShaGoods;
import com.dzy.demo.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsDao {
    @Select("Select g.*,mg.goods_id,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id=g.id")
    public List<GoodsVo> listMiaoShaGoods();

    @Select("Select g.*,mg.goods_id,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id=g.id where g.id=#{goodsId}")
    public GoodsVo getGoodsByGoodsId(@Param("goodsId") long goodsId);

    @Update("Update miaosha_goods set stock_count =stock_count-1 where goods_id=#{goodsId} and stock_count>0")
    int decryGoodsCount(MiaoShaGoods goods);
}
