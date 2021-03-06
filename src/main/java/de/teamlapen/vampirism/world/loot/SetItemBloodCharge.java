package de.teamlapen.vampirism.world.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import de.teamlapen.vampirism.api.items.IBloodChargeable;
import de.teamlapen.vampirism.util.REFERENCE;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.util.Random;

/**
 * Function to set the charge of any {@link de.teamlapen.vampirism.api.items.IBloodChargeable}
 */
class SetItemBloodCharge extends LootFunction {

    /**
     * In blood mB. Used if minCharge and maxCharge are -1
     */
    private final int charge;
    /**
     * In blood mB
     */
    private final int minCharge;
    /**
     * In blood mB
     */
    private final int maxCharge;

    /**
     * Either charge or (minCharge and maxCharge) should be -1
     */
    private SetItemBloodCharge(LootCondition[] conditionsIn, int charge, int minCharge, int maxCharge) {
        super(conditionsIn);
        this.charge = charge;
        this.minCharge = minCharge;
        this.maxCharge = maxCharge;
    }

    @Override
    public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
        if (charge > 0) {
            ((IBloodChargeable) stack.getItem()).charge(stack, charge);
        } else {
            ((IBloodChargeable) stack.getItem()).charge(stack, minCharge + rand.nextInt(maxCharge - minCharge));
        }
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetItemBloodCharge> {

        protected Serializer() {
            super(new ResourceLocation(REFERENCE.MODID, "set_item_blood_charge"), SetItemBloodCharge.class);
        }

        @Override
        public SetItemBloodCharge deserialize(JsonObject object, JsonDeserializationContext deserializationContext, LootCondition[] conditionsIn) {
            if (object.has("charge")) {
                int value = JsonUtils.getInt(object, "charge");
                return new SetItemBloodCharge(conditionsIn, Math.max(0, value), -1, -1);
            } else if (object.has("min_charge") && object.has("max_charge")) {
                int l = JsonUtils.getInt(object, "min_charge");
                int u = JsonUtils.getInt(object, "max_charge");
                return new SetItemBloodCharge(conditionsIn, -1, Math.max(0, l), Math.max(0, u));
            } else {
                throw new JsonSyntaxException("Need charge property for vampirism:set_item_blood_charge");
            }

        }

        @Override
        public void serialize(JsonObject object, SetItemBloodCharge functionClazz, JsonSerializationContext serializationContext) {
            if (functionClazz.charge != -1) {
                object.addProperty("charge", functionClazz.charge);
            } else {
                object.addProperty("min_charge", functionClazz.minCharge);
                object.addProperty("max_charge", functionClazz.maxCharge);
            }
        }
    }
}
