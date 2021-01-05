package org.csu.mypetstore.domain;

import org.csu.mypetstore.persistence.Impl.CartDAOImpl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Lory Simmons
 */
public class Cart implements Serializable {

  private static final long serialVersionUID = 8329559983943337176L;
  private final Map<String, CartItem> itemMap = Collections.synchronizedMap(new HashMap<String, CartItem>());
//  private final List<CartItem> itemList = new ArrayList<CartItem>();
  private String userId;
    public Cart(String userId) {
        this.userId = userId;
    }

    public Cart(Account account){
      this.userId = account.getUsername();
  }
    private final List<CartItem> itemList = new CartDAOImpl().getCart("j2ee");

  public Iterator<CartItem> getCartItems() {
    return itemList.iterator();
  }

  public List<CartItem> getCartItemList() {
    return itemList;
  }

  public int getNumberOfItems() {
      CartDAOImpl cartDAOImpl = new CartDAOImpl();
      return cartDAOImpl.getCart(userId).size();
//    return itemList.size();
  }

  public Iterator<CartItem> getAllCartItems() {
      CartDAOImpl cartDAOImpl = new CartDAOImpl();
      return cartDAOImpl.getCart(userId).iterator();
//    return itemList.iterator();
  }

  public boolean containsItemId(String itemId) {
    return itemMap.containsKey(itemId);
  }

  public void addItem(Item item, boolean isInStock, String userId) {
//    CartItem cartItem = (CartItem) itemMap.get(item.getItemId());
//    if (cartItem == null) {
//      cartItem = new CartItem();
//      cartItem.setItem(item);
//      cartItem.setQuantity(0);
//      cartItem.setInStock(isInStock);
//      itemMap.put(item.getItemId(), cartItem);
//      itemList.add(cartItem);
//    }
//    cartItem.incrementQuantity();
      userId=this.userId;
      CartDAOImpl cartDAOImpl = new CartDAOImpl();
      cartDAOImpl.addItem(userId,item.getItemId());
  }

  public void removeItemById(String itemId, String userId) {
    CartItem cartItem = (CartItem) itemMap.remove(itemId);
        CartDAOImpl cartDAOImpl = new CartDAOImpl();
        cartDAOImpl.removeItem(userId,itemId);
//      return cartItem.getItem();

  }

  public void incrementQuantityByItemId(String itemId) {
    CartItem cartItem = (CartItem) itemMap.get(itemId);
    cartItem.incrementQuantity();
  }

  public void setQuantityByItemId(String itemId, int quantity) {
    CartItem cartItem = (CartItem) itemMap.get(itemId);
    cartItem.setQuantity(quantity);
  }

  public BigDecimal getSubTotal() {
    BigDecimal subTotal = new BigDecimal("0");
    Iterator<CartItem> items = getAllCartItems();
    while (items.hasNext()) {
      CartItem cartItem = (CartItem) items.next();
      Item item = cartItem.getItem();
      BigDecimal listPrice = item.getListPrice();
      BigDecimal quantity = new BigDecimal(String.valueOf(cartItem.getQuantity()));
//      subTotal = subTotal.add(listPrice.multiply(quantity));
    }
    return subTotal;
  }

}
