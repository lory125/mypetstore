package org.csu.mypetstore.persistence.Impl;

import org.csu.mypetstore.domain.CartItem;
import org.csu.mypetstore.domain.Item;
import org.csu.mypetstore.persistence.CartDAO;
import org.csu.mypetstore.persistence.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * @author Lory Simmons
 *
 */
public class CartDAOImpl implements CartDAO {

    private static final String SELECT_CART_BY_USERID = "SELECT cartorder from cart WHERE id = ?";
    private static final String DELETE_CART_BY_USERID = "UPDATE cart SET cartorder = NULL WHERE id = ?";
        private static final String WRITE_CART_BY_USERID = "UPDATE cart SET cartorder = ? WHERE id = ?";
    //private final Map<String, CartItem> itemMap = Collections.synchronizedMap(new HashMap<String, CartItem>());

    @Override
    public List<CartItem> getCart(String userId) {
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CART_BY_USERID);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                List<CartItem> cartItemList = new ArrayList<>();

                String cartItems;
                String[] itemInfo;
                String cartOrder = resultSet.getString(1);
                StringTokenizer stringTokenizer = new StringTokenizer(cartOrder, "|");
                while (stringTokenizer.hasMoreTokens()){
                    cartItems = stringTokenizer.nextToken();
                    itemInfo = cartItems.split(":");
                    Item item = new Item();
                    item.setItemId(itemInfo[0]);
                    CartItem cartItem = new CartItem();
                    cartItem.setItem(item);
                    cartItem.setQuantity(Integer.parseInt(itemInfo[1]));
                    cartItemList.add(cartItem);
                }
                return cartItemList;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

//    public static void main(String[] args){
//        CartDAOImpl cartDAO = new CartDAOImpl();
//////        List<CartItem>items = cartDAO.getCart("j2ee");
////        cartDAO.clearCart("j2ee");
////        cartDAO.addItem("j2ee","t");
//        cartDAO.removeItem("j2ee","w");
//    }

    @Override
    public void addItem(String userId, String itemId) {
        boolean done = false;
        CartDAOImpl cartDAO = new CartDAOImpl();
        List<CartItem> items = cartDAO.getCart(userId);
        if(!items.isEmpty())
        {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getItem().getItemId().equals(itemId)) {
                    items.get(i).setQuantity(items.get(i).getQuantity() + 1);
                    writeTable(items, userId);
                    done = true;
                    break;
                }
            }
            if (!done) {
                Item item = new Item();
                item.setItemId(itemId);
                CartItem cartItem = new CartItem();
                cartItem.setItem(item);
                cartItem.setQuantity(Integer.parseInt("1"));
                items.add(cartItem);
                writeTable(items, userId);
            }
        }else{
            Item item = new Item();
            item.setItemId(itemId);
            CartItem cartItem = new CartItem();
            cartItem.setItem(item);
            cartItem.setQuantity(Integer.parseInt("1"));
            items.add(cartItem);
            writeTable(items, userId);
        }


    }

    @Override
    public void removeItem(String userId, String itemId) {
        CartDAOImpl cartDAO = new CartDAOImpl();
        List<CartItem> items = cartDAO.getCart(userId);
        for (int i = 0;i<items.size();i++){
            if (items.get(i).getItem().getItemId().equals(itemId)){
                items.remove(i);
                writeTable(items,userId);
                break;
            }
        }
    }

    @Override
    public void clearCart(String userId) {
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CART_BY_USERID);
            preparedStatement.setString(1, userId);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 向数据库中写入修改后的购物车
     * @param items
     * @param userId
     */
    public void writeTable(List<CartItem> items,String userId){
        String readyToWrite = "";
        for (int i = 0;i<items.size()-1;i++){
            readyToWrite = readyToWrite + items.get(i).getItem().getItemId()+":"+items.get(i).getQuantity()+"|";
        }
        readyToWrite = readyToWrite + items.get(items.size()-1).getItem().getItemId()+":"+items.get(items.size()-1).getQuantity();
        try{
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(WRITE_CART_BY_USERID);
            preparedStatement.setString(1, readyToWrite);
            preparedStatement.setString(2, userId);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
