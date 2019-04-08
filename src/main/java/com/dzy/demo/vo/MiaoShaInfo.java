package com.dzy.demo.vo;

import com.dzy.demo.domain.MiaoShaUser;

public class MiaoShaInfo {
    private MiaoShaUser user;
    private long goodsId;

    public MiaoShaUser getUser() {
        return user;
    }

    public void setUser(MiaoShaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "MiaoShaInfo{" +
                "user=" + user +
                ", goodsId=" + goodsId +
                '}';
    }
}
