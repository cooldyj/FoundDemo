package com.chinayszc.mobile.module.main;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import com.chinayszc.mobile.utils.Logs;

import java.lang.reflect.Field;

/**
 * Support a method to cancel BottomNavigation's Animation
 * Created by Jerry on 2017/3/25.
 */

class BottomNavigationViewHelper {

    /**
     * 调用反射禁止shiftingMode
     */
    static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Logs.e("ERROR NO SUCH FIELD: Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Logs.e("ERROR ILLEGAL ALG: Unable to change value of shift mode");
        }
    }

}
