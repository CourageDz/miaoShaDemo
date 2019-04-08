package com.dzy.demo.service;

import com.dzy.demo.dao.GoodsDao;
import com.dzy.demo.domain.Goods;
import com.dzy.demo.domain.MiaoShaGoods;
import com.dzy.demo.vo.GoodsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsService {
    @Resource
    GoodsDao goodsDao;

    public List<GoodsVo> listMiaoShaGoods(){
        return goodsDao.listMiaoShaGoods();
    }

    public GoodsVo getGoodsByGoodsId(long goodsId) {
        return goodsDao.getGoodsByGoodsId(goodsId);
    }

    public boolean decryGoodsCount(GoodsVo goodsVo) {
        MiaoShaGoods goods=new MiaoShaGoods();
        goods.setGoodsId(goodsVo.getId());
        int ret=goodsDao.decryGoodsCount(goods);
        return ret>0;
    }
}
