package hu.nvl.nvlblocks.data_classes;

import hu.nvl.nvlblocks.components.interfaces.NVLInterfaceLogger;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.ArrayList;
public class NVLRecipePattern implements NVLInterfaceLogger {
    private static final int[] NINE = new int[]{0,1,2,3,4,5,6,7,8};
    private static final int[] SIX = new int[]{0,1,3,4,6,7};
    private static final int[] THREE = new int[]{1,4,7};
    public boolean isCompacter = false;
    private final ArrayList<NVLItemStack> ingredients = new ArrayList<>();
    public NVLRecipePattern(CraftingRecipe recipe, int variant) {
        setRecipe(recipe,variant);
    }
    public NVLRecipePattern(String packedValues) {
        setFromString(packedValues);
    }
    public static int getRecipeNum(CraftingRecipe recipe) {
        int i = 0;
        for (Ingredient ing : recipe.getIngredients()) {
            if (ing.getItems().length > i) i = ing.getItems().length;
        }
        return i;
    }
    private void clear() {
        ingredients.clear();
    }
    public void setFromString(String value) {
        if (!value.isEmpty()) for (String part : value.split("#")) ingredients.add(new NVLItemStack(part));
    }
    public void setRecipe(CraftingRecipe recipe, int variant) {
 //       logLine("setRecipe:"+recipe+","+variant);
        ItemStack[] result = new ItemStack[9];
        if (variant < getRecipeNum(recipe)) {
            int num = recipe.getIngredients().size();
            int[] indexes = NINE;
            if (num == 6) indexes = SIX;
            if (num == 3) indexes = THREE;
  //          logLine(" num:"+num);
            int fid = -1;
            for (int i = 0;i < num;i++) {
                Ingredient ing = recipe.getIngredients().get(i);
                int l = ing.getItems().length;
                if (l > 0) {
                    NVLItemStack nStack = new NVLItemStack(variant < l?ing.getItems()[variant]:ing.getItems()[l-1]);
                    if (i == 0) fid = nStack.getId();
                    else if (fid != nStack.getId()) fid = -1;
                    nStack.setIndex(indexes[i]);
  //                  logLine(" nstack:"+nStack.getAsString());
                    ingredients.add(nStack);
                } else fid = -1;
            }
      //      logLine("setRecipe:"+recipe+","+variant+","+num+","+fid);
            isCompacter = (num == 9) && (fid > -1);
        }
    }
    public String getAsString() {
        String r = "";
        if (!ingredients.isEmpty()) {
            ArrayList<String> parts = new ArrayList<>();
            for (NVLItemStack ingredient : ingredients) parts.add(ingredient.getAsString());
            r = String.join("#",parts);
        }
        return r;
    }
    public ItemStack[] getPattern() {
        ItemStack[] result = new ItemStack[9];
        for (int i = 0;i < 9;i++) result[i] = ItemStack.EMPTY;
        for (int i = 0;i < ingredients.size();i++) {
            NVLItemStack t = ingredients.get(i);
            result[t.getIndex()] = t.getStack();
        };
        return result;
    }
    public int ingredientCount() {
        return ingredients.size();
    }
}
