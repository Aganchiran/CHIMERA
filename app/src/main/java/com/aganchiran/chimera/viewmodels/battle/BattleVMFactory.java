package com.aganchiran.chimera.viewmodels.battle;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.aganchiran.chimera.chimeracore.combat.CombatModel;

public class BattleVMFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private CombatModel combatModel;

    public BattleVMFactory(Application mApplication, CombatModel combatModel) {
        this.mApplication = mApplication;
        this.combatModel = combatModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BattleVM(mApplication, combatModel);
    }
}
