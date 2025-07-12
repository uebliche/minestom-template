package net.uebliche.utils.item;

import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.sound.SoundEvent;

public interface Equipment {

    EquipmentSlot getEquipmentSlot();

    SoundEvent getEquipSound();

    default String camaraOverlay() {
        return null;
    }

    default Boolean isDispensable() {
        return false;
    }

    default Boolean isSwappable() {
        return false;
    }

    default boolean damageOnHurt() {
        return false;
    }

    default boolean equipOnInteract() {
        return true;
    }

    default boolean canBeSheared() {
        return false;
    }

    default SoundEvent shearingSound() {
        return SoundEvent.ITEM_SHEARS_SNIP;
    }

}
