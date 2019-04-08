package com.dzy.demo.vo;

import com.dzy.demo.domain.Goods;

import java.util.Date;


public class GoodsVo extends Goods {
    private long goodsId;
    private double miaoShaPrice;
    private int stockCount;
    private Date startDate;
    private Date endDate;

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public double getMiaoShaPrice() {
        return miaoShaPrice;
    }

    public void setMiaoShaPrice(double miaoShaPrice) {
        this.miaoShaPrice = miaoShaPrice;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "GoodsVo{" +
                "goodsId=" + goodsId +
                ", miaoShaPrice=" + miaoShaPrice +
                ", stockCount=" + stockCount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
