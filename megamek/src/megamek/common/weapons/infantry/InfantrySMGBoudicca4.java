/**
 * MegaMek - Copyright (C) 2004,2005, 2022 MegaMekTeam
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */
 /*
 * Created on March 20, 2022
 * @author Hammer
 */

package megamek.common.weapons.infantry;

import megamek.common.AmmoType;

public class InfantrySMGBoudicca4 extends InfantryWeapon {

    /**
    *
    */
   private static final long serialVersionUID = -3164871600230559641L;

   public InfantrySMGBoudicca4() {
       super();

       name = "SMG (Boudicca-4)";
       setInternalName(name);
       addLookupName("Boudicca-4");
       ammoType = AmmoType.T_INFANTRY;
       bv = .27;
       tonnage =  0.0025;
       infantryDamage =  0.27;
       infantryRange =  1;
       ammoWeight =  0.0025;
       cost = 300;
       ammoCost =  20;
       shots =  30;
       bursts =  6;
       flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
       rulesRefs = "Shrapnel #5";
       techAdvancement
       .setTechBase(TECH_BASE_IS)
       .setTechRating(RATING_C)
       .setAvailability(RATING_D,RATING_D,RATING_D,RATING_C)
       .setISAdvancement(DATE_NONE, DATE_NONE,2530,DATE_NONE,DATE_NONE)
       .setISApproximate(false, false, true, false, false)
       .setProductionFactions(F_MC);


   }
}