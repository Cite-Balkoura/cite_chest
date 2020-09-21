package fr.milekat.cite_box.utils;

import fr.milekat.cite_box.obj.Crate;
import fr.milekat.cite_core.MainCore;
import fr.mrmicky.fastinv.FastInv;

public class DetailsGui extends FastInv {

    public DetailsGui(Crate crate) {
        super(63, MainCore.prefixCmd + "Détails de la box: " + crate.getName());
    }
}
