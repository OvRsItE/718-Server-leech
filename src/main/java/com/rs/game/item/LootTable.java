package com.rs.game.item;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author ReverendDread
 * Created 3/18/2021 at 7:51 PM
 * @project 718---Server
 */
public class LootTable {

    @Getter
    private int weight;

    private List<WeightedItem> guaranteed = Lists.newArrayList();
    private List<WeightedItem> items = Lists.newArrayList();

    public LootTable addGuaranteed(WeightedItem... items) {
        guaranteed.addAll(Arrays.asList(items));
        return this;
    }

    public LootTable addItem(WeightedItem item) {
        addItems(item);
        return this;
    }

    public LootTable addItems(WeightedItem... items) {
        for (WeightedItem item : items) {
            this.items.add(item);
            weight += item.getWeight();
        }
        return this;
    }

    public List<WeightedItem> getRandomItems(boolean allowGuarenteed) {
        List<WeightedItem> items = Lists.newArrayList();
        if (allowGuarenteed && !guaranteed.isEmpty()) {
            items.addAll(guaranteed);
        }
        double random = ThreadLocalRandom.current().nextDouble() * weight;
        for (WeightedItem item : this.items) {
            if (item.getWeight() == 0) {
                items.add(item);
                continue;
            }
            if ((random -= item.getWeight()) <= 0) {
                items.add(item);
                break;
            }
        }
        return items;
    }

    public WeightedItem getItem(int index) {
        return items.get(index);
    }

    public List<Item> getAsItems() {
        return items.stream().map(i -> new Item(i.getId(), i.getAmount())).collect(Collectors.toList());
    }

    public void shuffle() {
        Collections.shuffle(items);
    }

}
