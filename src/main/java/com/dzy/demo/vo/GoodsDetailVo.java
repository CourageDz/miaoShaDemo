package com.dzy.demo.vo;

import com.dzy.demo.domain.MiaoShaUser;

public class GoodsDetailVo {
    private GoodsVo goods;
    private int miaoShaStatus;
    private int remainSeconds;
    private MiaoShaUser user;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public int getMiaoShaStatus() {
        return miaoShaStatus;
    }

    public void setMiaoShaStatus(int miaoShaStatus) {
        this.miaoShaStatus = miaoShaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public MiaoShaUser getUser() {
        return user;
    }

    public void setUser(MiaoShaUser user) {
        this.user = user;
    }
}
