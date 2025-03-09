package com.rs.game.item;

import com.rs.Constants;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author ReverendDread
 * Created 3/18/2021 at 7:52 PM
 * @project 718---Server
 */
public class WeightedItem extends Item implements Serializable {

    private static final long serialVersionUID = -8509338052067356071L;

    @Getter
    private int weight;

    public void setWeight(double weight) {
        this.weight = (int) weight;
    }

    public WeightedItem(int id, int amount) {
        super(id, amount);
        this.weight = 0;
    }

    public WeightedItem(int id, int amount, int weight) {
        super(id, amount);
        this.weight = weight;
    }

    public static WeightedItem createOSRS(int id, int amount, int weight) {
        return new WeightedItem(id + Constants.OSRS_ITEMS_OFFSET, amount, weight);
    }

    public WeightedItem asOSRS() {
        boolean isOSRS = getId() >= Constants.OSRS_ITEMS_OFFSET;
        return new WeightedItem(isOSRS ? getId() : getId() + Constants.OSRS_ITEMS_OFFSET, getAmount(), weight);
    }

}
