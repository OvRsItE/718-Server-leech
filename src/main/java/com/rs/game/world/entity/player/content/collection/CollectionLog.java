package com.rs.game.world.entity.player.content.collection;

import com.google.common.collect.Lists;
import com.rs.game.item.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter @Setter
public class CollectionLog implements Serializable {

    private static final long serialVersionUID = 5379984848007176922L;

    private String name;
    private ItemCollectionManager.Category category;
    private List<String> aliases;
    private List<Item> required = Lists.newArrayList();
    private List<Item> acquired = Lists.newArrayList();
    private String type;
    private String arg;

    public boolean requires(Item item) {
        for (Item r : required) {
            if (r.getId() == item.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean collect(Item item) {
        for (Item i : acquired) {
            if (i.getId() == item.getId()) {
                i.setAmount(i.getAmount() + item.getAmount());
                return true;
            }
        }
        return acquired.add(item.clone());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionLog that = (CollectionLog) o;
        return Objects.equals(name, that.name) && category == that.category && Objects.equals(aliases, that.aliases) && Objects.equals(required, that.required) && Objects.equals(type, that.type);
    }

}
