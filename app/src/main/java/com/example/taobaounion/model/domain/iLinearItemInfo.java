package com.example.taobaounion.model.domain;

public interface iLinearItemInfo extends iBaseInfo {
    /**
     * 获取原价
     * @return
     */
    String getFinalPrice();

    /**
     * 获取优惠价格
     * @return
     */
    Long getCouponAmount();

    /**
     * 获取销量
     * @return
     */
    long getVolume();
}
