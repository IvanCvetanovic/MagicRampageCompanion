package xyz.magicrampagecompanion;

public final class StatsCalculator {
    private StatsCalculator() {}

    // --- Elixir knobs (same as yours) ---
    private static final class ElixirEffects {
        double damageMult = 1.0;
        double armorMult  = 1.0;
        double speedMult  = 1.0;
        double    speedAddPct = 1.0;
        double jumpMult   = 1.0;
        int    jumpAddPct = 0;
        int    pierceAdd  = 0;
        double cooldownMult = 1.0;
    }

    private static ElixirEffects resolveElixirEffects(Elixir e) {
        ElixirEffects fx = new ElixirEffects();
        if (e == null) return fx;

        int dmg = e.getDamageBonus();
        int arm = e.getArmorBonus();
        int spd = e.getSpeedBoost();

        fx.damageMult *= (1.0 + (dmg / 100.0));
        fx.armorMult  *= (1.0 + (arm / 100.0));
        fx.speedAddPct *= (1.0 + (spd / 100.0));

        return fx;
    }

    public static SetStats compute(EquipmentSet s) {
        SetStats out = new SetStats();
        if (s == null) return out;

        // Locals with null-safe defaults
        Armor armor = s.armor;
        Ring ring = s.ring;
        Weapon weapon = s.weapon;
        CharacterClass clazz = s.characterClass;
        boolean[] skills = (s.skills != null) ? s.skills : new boolean[36];

        Elements armorEl  = (s.armorElement  != null) ? s.armorElement  : Elements.NEUTRAL;
        Elements ringEl   = (s.ringElement   != null) ? s.ringElement   : Elements.NEUTRAL;
        Elements weaponEl = (s.weaponElement != null) ? s.weaponElement : Elements.NEUTRAL;

        ElixirEffects fx = resolveElixirEffects(s.elixir);

        // ---------- Attack cooldown & pierce ----------
        int currentAttackCooldown = (weapon != null) ? weapon.getAttackCooldown() : 1400; // assume slow if none
        currentAttackCooldown = (int) Math.round(currentAttackCooldown * fx.cooldownMult);

        int pierce = (weapon != null) ? weapon.getPierceCount() : 0;
        pierce += fx.pierceAdd;
        if (pierce < 0) pierce = 0;
        out.pierce = pierce;

        // ---------- Damage (guard weapon) ----------
        int damageOut = 0;
        if (weapon != null) {
            double dmg;
            int wUpgradesTotal = Math.max(weapon.getUpgrades(), 1);
            int wUpgradesChosen = Math.min(Math.max(s.weaponUpgrades, 0), wUpgradesTotal);

            if (wUpgradesChosen != 0) {
                dmg = weapon.getMinDamage()
                        + ((weapon.getMaxDamage() - weapon.getMinDamage()) / (double) wUpgradesTotal) * wUpgradesChosen;
            } else {
                dmg = weapon.getMinDamage();
            }

            // Armor magic if weapon element non-neutral
            if (weaponEl != Elements.NEUTRAL && armor != null) {
                dmg *= (1 + armor.getMagic() / 100.0);
            }

            // Armor weapon-type bonus
            if (armor != null) {
                switch (weapon.getType()) {
                    case SWORD:  dmg *= (1 + armor.getSword()  / 100.0); break;
                    case DAGGER: dmg *= (1 + armor.getDagger() / 100.0); break;
                    case STAFF:  dmg *= (1 + armor.getStaff()  / 100.0); break;
                    case AXE:    dmg *= (1 + armor.getAxe()    / 100.0); break;
                    case HAMMER: dmg *= (1 + armor.getHammer() / 100.0); break;
                    case SPEAR:  dmg *= (1 + armor.getSpear()  / 100.0); break;
                }
            }

            // Ring magic & type bonuses
            if (ring != null) {
                if (weaponEl != Elements.NEUTRAL) {
                    dmg *= (1 + ring.getMagic() / 100.0);
                }
                switch (weapon.getType()) {
                    case SWORD:  dmg *= (1 + ring.getSword()  / 100.0); break;
                    case DAGGER: dmg *= (1 + ring.getDagger() / 100.0); break;
                    case STAFF:  dmg *= (1 + ring.getStaff()  / 100.0); break;
                    case AXE:    dmg *= (1 + ring.getAxe()    / 100.0); break;
                    case HAMMER: dmg *= (1 + ring.getHammer() / 100.0); break;
                    case SPEAR:  dmg *= (1 + ring.getSpear()  / 100.0); break;
                }
            }

            // Class bonuses
            if (clazz != null) {
                dmg *= (1 + clazz.getMagicBonus() / 100.0);
                switch (weapon.getType()) {
                    case SWORD:  dmg *= (1 + clazz.getSwordBonus()  / 100.0); break;
                    case DAGGER: dmg *= (1 + clazz.getDaggerBonus() / 100.0); break;
                    case STAFF:  dmg *= (1 + clazz.getStaffBonus()  / 100.0); break;
                    case AXE:    dmg *= (1 + clazz.getAxeBonus()    / 100.0); break;
                    case HAMMER: dmg *= (1 + clazz.getHammerBonus() / 100.0); break;
                    case SPEAR:  dmg *= (1 + clazz.getSpearBonus()  / 100.0); break;
                }
            }

            // Skill tree bonuses by weapon type
            if (weapon.getType().equals(WeaponTypes.SWORD)  && skillsSafe(skills, 1))  dmg *= 1.15;
            if (weapon.getType().equals(WeaponTypes.DAGGER) && skillsSafe(skills, 2))  dmg *= 1.20;
            if (weapon.getType().equals(WeaponTypes.STAFF)  && skillsSafe(skills, 13)) dmg *= 1.20;
            if (weapon.getType().equals(WeaponTypes.SPEAR)  && skillsSafe(skills, 14)) dmg *= 1.40;
            if (weapon.getType().equals(WeaponTypes.HAMMER) && skillsSafe(skills, 25)) dmg *= 1.60;
            if (weapon.getType().equals(WeaponTypes.AXE)    && skillsSafe(skills, 26)) dmg *= 1.50;

            // Element synergies
            if (weaponEl == armorEl && weaponEl != Elements.NEUTRAL) dmg *= 1.25;
            if (weaponEl == ringEl  && weaponEl != Elements.NEUTRAL) dmg *= 1.20;

            // Elixir multiplier
            dmg *= fx.damageMult;

            damageOut = (int) Math.ceil(dmg);
        }
        out.damage = damageOut;

        // ---------- Armor ----------
        int armorOut = 0;
        if (armor != null) {
            int aUpgradesTotal = Math.max(armor.getUpgrades(), 1);
            int aUpgradesChosen = Math.min(Math.max(s.armorUpgrades, 0), aUpgradesTotal);

            double armorVal = armor.getMinArmor()
                    + ((armor.getMaxArmor() - armor.getMinArmor()) / (double) aUpgradesTotal) * aUpgradesChosen;

            // Add ring’s flat armor (even if armor is null we handled that above)
            if (ring != null) armorVal += ring.getArmor();

            // Multipliers
            if (ring != null)   armorVal *= (1 + ring.getArmorBonus() / 100.0);
            if (weapon != null) armorVal *= (1 + weapon.getArmorBonus() / 100.0);
            if (clazz != null)  armorVal *= (1 + clazz.getArmorBonus() / 100.0);
            if (skillsSafe(skills, 25)) armorVal *= 1.20;

            // Elixir multiplier
            armorVal *= fx.armorMult;

            armorOut = (int) Math.round(armorVal);
        } else {
            // No armor piece: you may still want to count ring's flat armor
            double armorVal = 0.0;
            if (ring != null) armorVal += ring.getArmor();
            if (ring != null)   armorVal *= (1 + ring.getArmorBonus() / 100.0);
            if (weapon != null) armorVal *= (1 + weapon.getArmorBonus() / 100.0);
            if (clazz != null)  armorVal *= (1 + clazz.getArmorBonus() / 100.0);
            if (skillsSafe(skills, 18)) armorVal *= 1.25;
            armorVal *= fx.armorMult;
            armorOut = (int) Math.round(armorVal);
        }
        out.armor = armorOut;

        // ---------- Attack speed stars ----------
        int stars;
        if      (currentAttackCooldown <= 300) stars = 5;
        else if (currentAttackCooldown <= 450) stars = 4;
        else if (currentAttackCooldown <= 650) stars = 3;
        else if (currentAttackCooldown <= 750) stars = 2;
        else                                   stars = 1;
        out.attackSpeedStars = stars;

        // ---------- Speed & Jump ----------
        double aSpd = (armor  != null ? armor.getSpeed()  : 0) / 100.0 + 1;
        double wSpd = (weapon != null ? weapon.getSpeed() : 0) / 100.0 + 1;
        double rSpd = (ring   != null ? ring.getSpeed()   : 0) / 100.0 + 1;

        double speedPct = (aSpd * wSpd * rSpd) * 100 - 100;
        if (clazz != null) speedPct += clazz.getSpeedBonus();
        if (skillsSafe(skills, 0)) speedPct += 4;
        speedPct = (int) (Math.round(speedPct * fx.speedMult) * fx.speedAddPct);
        out.speedPct = (int) speedPct;

        double aJump = (armor  != null ? armor.getJump()  : 0) / 100.0 + 1;
        double wJump = (weapon != null ? weapon.getJump() : 0) / 100.0 + 1;
        double rJump = (ring   != null ? ring.getJump()   : 0) / 100.0 + 1;
        double cJump = (clazz  != null ? clazz.getJumpImpulseBonus() : 0) / 100.0 + 1;

        double jump = aJump * rJump * wJump * cJump;
        if (skillsSafe(skills, 12)) jump += 0.03;
        jump = Math.floor(jump * 100.0) / 100.0;
        jump = jump * 100 - 100;
        jump = Math.round(jump * fx.jumpMult) + fx.jumpAddPct;
        out.jumpPct = (int) jump;

        // ---------- Flags ----------
        out.fireRes = (ringEl == Elements.FIRE)
                || out.armor >= 330
                || (armorEl == Elements.FIRE)
                || skillsSafe(skills, 16);

        out.frostRes    = (armor != null && armor.isFrostImmune());
        out.spikeRes    = (ringEl == Elements.AIR || ringEl == Elements.WATER
                || armorEl == Elements.AIR || armorEl == Elements.WATER
                || skillsSafe(skills, 29));
        out.projPersist = (weapon != null && weapon.isPersistAgainstProjectile());
        out.poisonAttack= (weapon != null && weapon.isPoisonous());
        out.frostAttack = (weapon != null && weapon.isFrost());

        return out;
    }

    private static boolean skillsSafe(boolean[] skills, int idx) {
        return skills != null && idx >= 0 && idx < skills.length && skills[idx];
    }
}
