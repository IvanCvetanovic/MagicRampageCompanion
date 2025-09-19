package xyz.magicrampagecompanion;

import xyz.magicrampagecompanion.Elements;
import xyz.magicrampagecompanion.WeaponTypes;

public final class StatsCalculator {
    private StatsCalculator() {}

    public static SetStats compute(EquipmentSet s) {
        SetStats out = new SetStats();

        if (s == null || s.armor == null || s.weapon == null || s.ring == null || s.characterClass == null || s.skills == null) {
            return out;
        }

        // --- Attack cooldown & pierce ---
        int currentAttackCooldown = s.weapon.getAttackCooldown();
        out.pierce = s.weapon.getPierceCount();

        // --- Damage base (with weapon upgrades) ---
        double dmg;
        if (s.weaponUpgrades != 0) {
            dmg = (s.weapon.getMinDamage()
                    + ((s.weapon.getMaxDamage() - s.weapon.getMinDamage())
                    / ((double) s.weapon.getUpgrades())) * s.weaponUpgrades);
        } else {
            dmg = s.weapon.getMinDamage();
        }

        // Armor magic bonus if weapon is non-neutral
        if (!s.weaponElement.equals(Elements.NEUTRAL)) {
            dmg = dmg * (1 + s.armor.getMagic() / 100.0);
        }

        // Armor weapon-type bonus
        switch (s.weapon.getType()) {
            case SWORD:  dmg *= (1 + s.armor.getSword()  / 100.0); break;
            case DAGGER: dmg *= (1 + s.armor.getDagger() / 100.0); break;
            case STAFF:  dmg *= (1 + s.armor.getStaff()  / 100.0); break;
            case AXE:    dmg *= (1 + s.armor.getAxe()    / 100.0); break;
            case HAMMER: dmg *= (1 + s.armor.getHammer() / 100.0); break;
            case SPEAR:  dmg *= (1 + s.armor.getSpear()  / 100.0); break;
        }

        // Ring magic & type bonuses
        if (!s.weaponElement.equals(Elements.NEUTRAL)) {
            dmg *= (1 + s.ring.getMagic() / 100.0);
        }
        switch (s.weapon.getType()) {
            case SWORD:  dmg *= (1 + s.ring.getSword()  / 100.0); break;
            case DAGGER: dmg *= (1 + s.ring.getDagger() / 100.0); break;
            case STAFF:  dmg *= (1 + s.ring.getStaff()  / 100.0); break;
            case AXE:    dmg *= (1 + s.ring.getAxe()    / 100.0); break;
            case HAMMER: dmg *= (1 + s.ring.getHammer() / 100.0); break;
            case SPEAR:  dmg *= (1 + s.ring.getSpear()  / 100.0); break;
        }

        // Class bonuses
        dmg *= (1 + s.characterClass.getMagicBonus() / 100.0);
        switch (s.weapon.getType()) {
            case SWORD:  dmg *= (1 + s.characterClass.getSwordBonus()  / 100.0); break;
            case DAGGER: dmg *= (1 + s.characterClass.getDaggerBonus() / 100.0); break;
            case STAFF:  dmg *= (1 + s.characterClass.getStaffBonus()  / 100.0); break;
            case AXE:    dmg *= (1 + s.characterClass.getAxeBonus()    / 100.0); break;
            case HAMMER: dmg *= (1 + s.characterClass.getHammerBonus() / 100.0); break;
            case SPEAR:  dmg *= (1 + s.characterClass.getSpearBonus()  / 100.0); break;
        }

        // Skill tree bonuses by weapon type
        if (s.skills[1]  && s.weapon.getType().equals(WeaponTypes.SWORD))  dmg *= 1.15;
        if (s.skills[2]  && s.weapon.getType().equals(WeaponTypes.DAGGER)) dmg *= 1.20;
        if (s.skills[13] && s.weapon.getType().equals(WeaponTypes.STAFF))  dmg *= 1.20;
        if (s.skills[14] && s.weapon.getType().equals(WeaponTypes.SPEAR))  dmg *= 1.40;
        if (s.skills[25] && s.weapon.getType().equals(WeaponTypes.HAMMER)) dmg *= 1.60;
        if (s.skills[26] && s.weapon.getType().equals(WeaponTypes.AXE))    dmg *= 1.50;

        // Element synergies
        if (s.weaponElement.equals(s.armorElement)) dmg *= 1.25;
        if (s.weaponElement.equals(s.ringElement))  dmg *= 1.20;

        dmg = Math.ceil(dmg);
        out.damage = (int) dmg;

        // --- Armor (respect upgrades) ---
        if (s.armor.getUpgrades() == 0) s.armor.setUpgrades(1);
        double armorVal = (s.armor.getMinArmor()
                + ((s.armor.getMaxArmor() - s.armor.getMinArmor())
                / ((double) s.armor.getUpgrades())) * s.armorUpgrades);

        armorVal += s.ring.getArmor();
        armorVal *= (1 + s.ring.getArmorBonus() / 100.0);
        armorVal *= (1 + s.weapon.getArmorBonus() / 100.0);
        armorVal *= (1 + s.characterClass.getArmorBonus() / 100.0);
        if (s.skills[18]) armorVal *= 1.25;

        armorVal = Math.round(armorVal);
        if (s.armor.getUpgrades() == 0) armorVal = s.armor.getMinArmor();
        out.armor = (int) armorVal;

        // --- Attack speed → star rating ---
        int stars;
        if (currentAttackCooldown <= 300)      stars = 5;
        else if (currentAttackCooldown <= 450) stars = 4;
        else if (currentAttackCooldown <= 650) stars = 3;
        else if (currentAttackCooldown <= 750) stars = 2;
        else                                   stars = 1;
        out.attackSpeedStars = stars;

        // --- Speed & Jump ---
        double aSpd = s.armor.getSpeed()  / 100.0 + 1;
        double wSpd = s.weapon.getSpeed() / 100.0 + 1;
        double rSpd = s.ring.getSpeed()   / 100.0 + 1;

        int speedPct = (int) ((aSpd * wSpd * rSpd) * 100 - 100);
        speedPct += s.characterClass.getSpeedBonus();
        if (s.skills[0]) speedPct += 4;
        out.speedPct = speedPct;

        double aJump = s.armor.getJump()  / 100.0 + 1;
        double wJump = s.weapon.getJump() / 100.0 + 1;
        double rJump = s.ring.getJump()   / 100.0 + 1;
        double cJump = s.characterClass.getJumpImpulseBonus() / 100.0 + 1;

        double jump = aJump * rJump * wJump * cJump;
        if (s.skills[12]) jump += 0.03;
        jump = Math.floor(jump * 100.0) / 100.0;
        jump = jump * 100 - 100;
        out.jumpPct = (int) jump;

        // --- Flags for checkmarks ---
        out.fireRes = (Elements.FIRE.equals(s.ringElement))
                || out.armor >= 330
                || Elements.FIRE.equals(s.armorElement)
                || s.skills[16];

        out.frostRes     = s.armor.isFrostImmune();
        out.spikeRes     = Elements.AIR.equals(s.ringElement) || Elements.WATER.equals(s.ringElement)
                || Elements.AIR.equals(s.armorElement) || Elements.WATER.equals(s.armorElement)
                || s.skills[29];
        out.projPersist  = s.weapon.isPersistAgainstProjectile();
        out.poisonAttack = s.weapon.isPoisonous();
        out.frostAttack  = s.weapon.isFrost();

        // --- Composite rating (capped 0..10) ---
        double skillsSum =
                ((s.skills[3] ? .1 : 0) + (s.skills[4] ? .1 : 0) + (s.skills[5] ? .1 : 0) + (s.skills[6] ? .1 : 0) +
                        (s.skills[7] ? .1 : 0) + (s.skills[8] ? .1 : 0) + (s.skills[9] ? .1 : 0) + (s.skills[10] ? .1 : 0) +
                        (s.skills[11] ? .1 : 0) + (s.skills[15] ? .1 : 0) + (s.skills[17] ? .1 : 0) + (s.skills[18] ? .1 : 0) +
                        (s.skills[19] ? .1 : 0) + (s.skills[20] ? .1 : 0) + (s.skills[21] ? .1 : 0) + (s.skills[22] ? .1 : 0) +
                        (s.skills[23] ? .1 : 0) + (s.skills[27] ? .1 : 0) + (s.skills[28] ? .1 : 0) + (s.skills[30] ? .1 : 0) +
                        (s.skills[31] ? .1 : 0) + (s.skills[32] ? .1 : 0) + (s.skills[33] ? .1 : 0) + (s.skills[34] ? .1 : 0) + (s.skills[35] ? .1 : 0)) +

                        (((Elements.FIRE.equals(s.ringElement)) || (Elements.FIRE.equals(s.armorElement)) || (s.skills[13])) ? 0.2 : 0) +
                        (((Elements.AIR.equals(s.ringElement) || Elements.WATER.equals(s.ringElement)) ||
                                (Elements.AIR.equals(s.armorElement) || Elements.WATER.equals(s.armorElement)) || (s.skills[29])) ? 0.2 : 0);

        double rating = ((out.damage / 5000.0)
                + (out.armor / 150.0)
                + (out.speedPct / 25.0)
                + (out.jumpPct / 25.0)
                + skillsSum
                + (out.pierce / 10.0)
                + ((currentAttackCooldown - 1400) / 700.0)
                + (s.weapon.isFrost() ? 0.15 : 0)
                + (s.weapon.isPoisonous() ? 0.15 : 0));

        if (rating > 10) rating = 10;
        if (rating < 0)  rating = 0;
        out.rating = rating;

        return out;
    }
}
