package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapelessRecipes implements IRecipe {

    private final ItemStack result;
    private final List ingredients;

    public ShapelessRecipes(ItemStack itemstack, List list) {
        this.result = itemstack;
        this.ingredients = list;
    }

    public ItemStack b() {
        return this.result;
    }

    public boolean a(InventoryCrafting inventorycrafting, World world) {
        ArrayList arraylist = new ArrayList(this.ingredients);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                ItemStack itemstack = inventorycrafting.b(j, i);

                if (itemstack != null) {
                    boolean flag = false;
                    Iterator iterator = arraylist.iterator();

                    while (iterator.hasNext()) {
                        ItemStack itemstack1 = (ItemStack) iterator.next();

                        if (itemstack.id == itemstack1.id && (itemstack1.getData() == 32767 || itemstack.getData() == itemstack1.getData())) {
                            flag = true;
                            arraylist.remove(itemstack1);
                            break;
                        }
                    }

                    if (!flag) {
                        return false;
                    }
                }
            }
        }

        return arraylist.isEmpty();
    }

    public ItemStack a(InventoryCrafting inventorycrafting) {
        return this.result.cloneItemStack();
    }

    public int a() {
        return this.ingredients.size();
    }
}
