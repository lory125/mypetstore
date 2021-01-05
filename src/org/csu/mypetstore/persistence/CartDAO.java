package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.CartItem;

import java.util.List;

public interface CartDAO {
    /**
     * 获取购物车内容
     * @param userId
     * @return
     */
    List<CartItem> getCart(String userId);

    /**
     * 向购物车中添加物品
     * @param userId
     * @param itemId
     */
    void addItem(String userId,String itemId);

    /**
     * 从购物车中移除单一产品
     * @param userId
     * @param itemId
     */
    void removeItem(String userId,String itemId);

    /**
     * 清空购物车
     * @param userId
     */
    void clearCart(String userId);

}
